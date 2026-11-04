#include <bmp.h>
#include <bmp_header.h>
#include <image.h>
#include <stdio.h>

static const uint16_t BMP_HEADER_TYPE = 0x4D42;
static const uint32_t BMP_HEADER_SIZE = 54; 
static const uint32_t BITMAP_INFO_HEADER_SIZE = 40; 
static const uint16_t BMP_COLOR_DEPTH = 24;

enum read_status from_bmp( FILE* in, struct image* img )
{
    if ( in == NULL || img == NULL )
        return READ_INVALID_HEADER;

    struct bmp_header header;

    const size_t read_bytes = fread( &header, sizeof( struct bmp_header ), 1, in );
    if( read_bytes != 1 )
        return READ_INVALID_HEADER;
    if( header.bfType != BMP_HEADER_TYPE )
        return READ_INVALID_SIGNATURE;
    if( header.biBitCount != BMP_COLOR_DEPTH )
        return READ_INVALID_BITS;
    if( header.bfileSize == 0 )
        return ZERO_SIZE;
    if ( header.biCompression != 0 || header.biWidth == 0 || header.biHeight == 0 )
        return READ_INVALID_HEADER;

    image_destroy(img);
    *img = create_image( header.biWidth, header.biHeight );
    if( img->data == NULL )
        return READ_MEMORY_ERROR;

    const uint32_t row_size_in_bytes = img->width * sizeof( struct pixel );   // Ширина одной строки в байтах без паддинга
    const uint32_t padded_row_size = ( row_size_in_bytes + 3 ) & ( ~3 );      // Ближайшее число, кратное 4
    const uint32_t padding = padded_row_size - row_size_in_bytes;             // Количество байт мусора (padding) в конце каждой строки

    for( uint64_t y = img->height; y > 0; y-- ) 
    {
        struct pixel* row_ptr = get_pixel( img, 0, y - 1 );

        if( fread( row_ptr, 1, row_size_in_bytes, in ) != row_size_in_bytes ) 
        {
            image_destroy( img );
            return READ_INVALID_BITS;
        }

        if( padding > 0 ) 
        {
            char trash[3]; 
            if( fread( trash, 1, padding, in ) != padding ) 
            {
                image_destroy( img );
                return READ_INVALID_BITS;
            }
        }
    }

    return READ_OK;
}

enum write_status to_bmp( FILE* out, struct image* img )
{
    if( out == NULL || img == NULL || img->data == NULL ) 
        return WRITE_ERROR;
    
    const uint32_t width = img->width;
    const uint32_t height = img->height;
    const uint8_t padding = (4 - (img->width * sizeof(struct pixel)) % 4) % 4;

    const uint32_t row_size = width * sizeof(struct pixel) + padding;
    const uint32_t image_size = row_size * height;

    const uint32_t header_size = BMP_HEADER_SIZE;
    const uint32_t file_size = header_size + image_size;

    const uint16_t bfType = BMP_HEADER_TYPE;
    const uint32_t bfSize = file_size;
    const uint16_t bfReserved1 = 0;
    const uint16_t bfReserved2 = 0;
    const uint32_t bfOffBits = header_size;

    if( fwrite( &bfType, sizeof(bfType), 1, out ) != 1 ||
        fwrite( &bfSize, sizeof(bfSize), 1, out ) != 1 ||
        fwrite( &bfReserved1, sizeof(bfReserved1), 1, out ) != 1 ||
        fwrite( &bfReserved2, sizeof(bfReserved2), 1, out ) != 1 ||
        fwrite( &bfOffBits, sizeof(bfOffBits), 1, out ) != 1) 
    {
        return WRITE_ERROR;
    }

    const uint32_t biSize = BITMAP_INFO_HEADER_SIZE;
    const int32_t  biWidth = (int32_t)width;
    const int32_t  biHeight = (int32_t)height;
    const uint16_t biPlanes = 1;
    const uint16_t biBitCount = BMP_COLOR_DEPTH; 
    const uint32_t biCompression = 0;
    const uint32_t biSizeImage = image_size;
    const int32_t  biXPelsPerMeter = 0;
    const int32_t  biYPelsPerMeter = 0;
    const uint32_t biClrUsed = 0;
    const uint32_t biClrImportant = 0;

    if( fwrite( &biSize, sizeof(biSize), 1, out ) != 1 ||
        fwrite( &biWidth, sizeof(biWidth), 1, out ) != 1 ||
        fwrite( &biHeight, sizeof(biHeight), 1, out ) != 1 ||
        fwrite( &biPlanes, sizeof(biPlanes), 1, out ) != 1 ||
        fwrite( &biBitCount, sizeof(biBitCount), 1, out ) != 1 ||
        fwrite( &biCompression, sizeof(biCompression), 1, out ) != 1 ||
        fwrite( &biSizeImage, sizeof(biSizeImage), 1, out ) != 1 ||
        fwrite( &biXPelsPerMeter, sizeof(biXPelsPerMeter), 1, out ) != 1 ||
        fwrite( &biYPelsPerMeter, sizeof(biYPelsPerMeter), 1, out ) != 1 ||
        fwrite( &biClrUsed, sizeof(biClrUsed), 1, out ) != 1 ||
        fwrite( &biClrImportant, sizeof(biClrImportant), 1, out ) != 1 ) 
    {
        return WRITE_ERROR;
    }

    uint8_t zero_padding[3] = {0, 0, 0};

    for( uint64_t y = height; y > 0; y-- ) 
    {
        struct pixel* row_start = get_pixel( img, 0, y - 1 );
        if( fwrite(row_start, sizeof(struct pixel), width, out) != width ) 
            return WRITE_ERROR;
        
        if( padding > 0 ) 
            if( fwrite(zero_padding, 1, padding, out) != padding ) 
                return WRITE_ERROR;
    }

    return WRITE_OK;
}
