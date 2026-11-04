#include <image.h>
#include <malloc.h>
#include <stdint.h>

struct image create_image( const uint32_t width, const uint32_t height )
{
    struct image img;
    img.width = width;
    img.height = height;
    img.data = malloc( (uint64_t)width * height * sizeof( struct pixel ) );
    return img;    
}

void image_destroy( struct image* img )
{
    if( img && img->data )
    {
        free( img->data );
        img->data = NULL;
        img->width = 0;
        img->height = 0;
    }
}

struct pixel* get_pixel( struct image* img, const size_t x, const size_t y )
{
    if( img == NULL || img->data == NULL ) 
        return NULL; 
    if( x >= img->width || y >= img->height ) 
        return NULL;
    return &( img->data[y * img->width + x] );
}
