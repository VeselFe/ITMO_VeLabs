#include <transformation.h>

#define ERROR_IMAGE ((struct image){0})

static struct image transposition( struct image* img )
{
    if ( img == NULL || img->data == NULL ) return ERROR_IMAGE;
    
    struct image new_matrix = create_image( img->height, img->width );
    if ( new_matrix.data == NULL ) return ERROR_IMAGE;

    for( uint32_t y = 0; y < img->height; y++ )
        for( uint32_t x = 0; x < img->width; x++ )
        {
            *get_pixel( &new_matrix, y, x ) = *get_pixel( img, x, y );
        }

    return new_matrix;
}

struct image rotation( struct image* img, char direction )
{
    if( img == NULL || img->data == NULL ) return ERROR_IMAGE;

    struct image mirrored_image;

    if( direction == 'l' )
        mirrored_image = horizontal_mirror( img );
    else if( direction == 'r' )
        mirrored_image = vertical_mirror( img );
    else
        return ERROR_IMAGE;

    if( mirrored_image.data == NULL ) 
        return ERROR_IMAGE; 
    
    const struct image result = transposition( &mirrored_image );
    
    image_destroy( &mirrored_image ); 
    
    return result;
}
