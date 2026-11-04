#pragma once

#include <stdio.h>

FILE* open_file( char* file_name, char* mode );
void close_file( FILE* file );
