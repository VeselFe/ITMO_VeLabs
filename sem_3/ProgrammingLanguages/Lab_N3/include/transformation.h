#pragma once

#include <image.h>
#include <stdint.h>

struct image rotation( struct image* img, char direction );

struct image horizontal_mirror( struct image* img );
struct image vertical_mirror( struct image* img );

