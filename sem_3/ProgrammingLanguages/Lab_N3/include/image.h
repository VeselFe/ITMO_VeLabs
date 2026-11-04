#pragma once

#include <malloc.h>
#include <stdint.h>

struct pixel 
{
  uint8_t components[3];
};

struct image 
{
  uint32_t width, height; 
  struct pixel* data;
};

struct pixel* get_pixel( struct image* img, const size_t x, const size_t y );
struct image create_image( const uint32_t width, const uint32_t height );
void image_destroy( struct image* img );
