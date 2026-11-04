#include <bmp.h>
#include <errno.h>
#include <fileio.h>
#include <stdio.h>
#include <strlib.h>
#include <transformation.h>

int main( int argc, char** argv ) 
{    
    if( argc != 4 )
    {
        fprintf(stderr, "Некорректный ввод аргументов для использования программы. \n Ожидаемый ввод: %s <source-image> <transformed-image> <tranformation> \n", argv[0]);
        return EINVAL;
    }

    char* input_file_name = argv[1];
    char* output_file_name = argv[2];
    char* transformation = argv[3];
    
    FILE* input_file = open_file( input_file_name, "rb" );
    if( input_file == NULL )
    {
        fprintf(stderr, "Ошибка открытия файла. \n");
        return ENOENT;
    }
    
    struct image image = {0};    // Инициализация структуры под наше новое изображение
    enum read_status rdst = from_bmp( input_file, &image );
    if( rdst != 0 )
    {
        fprintf(stderr, "Ошибка чтения содержания bmp файла. \n");
        close_file( input_file );
        return rdst;
    }
    close_file( input_file );

    struct image res_image = {0};    // Инициализация структуры под наше измененное изображение
    if( equals(transformation, "none") ) 
    {
        res_image = image;
    }    
    else
    {
        if( equals(transformation, "cw90") )
            res_image = rotation( &image, 'r' );
        else if( equals(transformation, "ccw90") )
            res_image = rotation( &image, 'l' );
        else if( equals(transformation, "fliph") )
            res_image = horizontal_mirror( &image );
        else if( equals(transformation, "flipv") )
            res_image = vertical_mirror( &image );
        else
        {
            fprintf(stderr, "Указан некорректный вид трансформации. \n");
            image_destroy( &image );
            return EINVAL;
        }
        
        image_destroy( &image );
    }
    
    if ( res_image.data == NULL )
    {
        fprintf( stderr, "Не удалось выполнить трансформацию. \n" );
        image_destroy( &res_image );
        return ENOMEM;           
    }
    
    FILE* output_file = open_file( output_file_name, "wb" );
    if( output_file == NULL )
    {
        fprintf(stderr, "Ошибка открытия файла. \n");
        image_destroy( &res_image );
        return EIO;
    }

    to_bmp( output_file, &res_image );
    
    close_file( output_file );
    image_destroy( &res_image );

    return 0;
}
