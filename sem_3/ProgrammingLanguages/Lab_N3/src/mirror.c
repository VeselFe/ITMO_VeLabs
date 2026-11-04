#include <image.h>
#include <transformation.h>

#define ERROR_IMAGE ((struct image){0})

struct image horizontal_mirror( struct image* img )
{
    if ( img == NULL || img->data == NULL ) return ERROR_IMAGE;

    struct image new_image = create_image( img->width, img->height );
    if ( new_image.data == NULL ) return ERROR_IMAGE;

    for( uint32_t x = 0; x < new_image.width; x++ )
    {
        uint32_t mir_x = new_image.width - 1 - x;
        for( uint32_t y = 0; y < new_image.height; y++ )
        {
            *get_pixel( &new_image, mir_x, y ) = *get_pixel( img, x, y ); 
        }
    }

    return new_image;
}

struct image vertical_mirror( struct image* img )
{
    if ( img == NULL || img->data == NULL ) return ERROR_IMAGE;

    struct image new_image = create_image( img->width, img->height );
    if ( new_image.data == NULL ) return ERROR_IMAGE;

    for( uint32_t y = 0; y < new_image.height; y++ )
    {
        uint32_t mir_y = new_image.height - 1 - y;
        for( uint32_t x = 0; x < new_image.width; x++ )
        {
            *get_pixel( &new_image, x, mir_y ) = *get_pixel( img, x, y ); 
        }
    }

    return new_image;
}
