#include <fileio.h>

FILE* open_file( char* file_name, char* mode )
{
    FILE* new_file = fopen( file_name, mode );
    if( new_file == NULL )
        return NULL;
    return new_file;
}

void close_file( FILE* file )
{
    if( file == NULL )
        return;
    fclose( file );
}
