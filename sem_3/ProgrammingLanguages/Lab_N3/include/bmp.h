#pragma once

#include <errno.h>
#include <image.h>
#include <stdio.h>

enum read_status 
{
    READ_OK = 0,
    READ_INVALID_SIGNATURE,
    READ_INVALID_BITS,
    READ_INVALID_HEADER,
    ZERO_SIZE,
    READ_ERROR,
    READ_OUT_OF_MEMORY,
    READ_OPEN_ERROR,
    READ_MEMORY_ERROR = ENOMEM
};

enum write_status 
{
    WRITE_OK = 0,
    WRITE_ERROR,
    WRITE_OPEN_ERROR
};

enum read_status from_bmp( FILE* in, struct image* img );
enum write_status to_bmp( FILE* out, struct image* img );

enum read_status from_bmp_file( char const* filename, struct image* img );
enum write_status to_bmp_file( char const* filename, struct image const* img );
