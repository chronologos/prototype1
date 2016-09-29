"""Imagetiler script for Reschu

Setup Instructions:
    1. Create tiles/ dir in current working directory.
    2. Move image to working directory.
    3. On OSX: pip install Pillow. On Windows: install Pillow binary.
    4. Configure __main__ in this file to initialize with appropriate tile size, overlap and filename.
    5. Run script: python PrototypeInageTiler.py
"""
import os
from PIL import Image


class ImageTiler(object):
    """
    This class is meant to do the work of taking a large png file and splitting it into smalltiles with overlap. By default, it writes to the tiles/ in the current working directory.
    """

    def __init__(self, tile_size, overlap, filename):
        print "Initializing Tiler..."
        self.tile_size = tile_size
        self.overlap = overlap
        self.img = Image.open(filename)
        self.img_width, self.img_height = self.img.size
        self.final_x_tile_size = self.tile_size
        self.final_y_tile_size = self.tile_size
        self.coordinates = []

    def ComputeCoordinates(self):
        print "Computing coordinates and sizes of tiles."
        x_coords, y_coords = [], []
        curr_y, curr_x = self.overlap, self.overlap
        while curr_x < self.img_width:
            next_x = curr_x - self.overlap
            x_coords.append(next_x)
            curr_x = next_x + self.tile_size
        if curr_x > self.img_width:
            self.final_x_tile_size = self.img_width - x_coords[-1]
        while curr_y < self.img_height:
            next_y = curr_y - self.overlap
            y_coords.append(next_y)
            curr_y = next_y + self.tile_size
        if curr_y > self.img_height:
            self.final_y_tile_size = self.img_height - y_coords[-1]
        print "img w: %d, h: %d" % (self.img_width, self.img_height)
        # print x_coords, y_coords, self.final_x_tile_size,
        # self.final_y_tile_size
        for index_x, x in enumerate(x_coords):
            for index_y, y in enumerate(y_coords):
                if index_x == len(x_coords) - \
                        1 and index_y == len(y_coords) - 1:
                    self.coordinates.append(
                        (x, y, self.final_x_tile_size, self.final_y_tile_size))
                elif index_x == len(x_coords) - 1:
                    self.coordinates.append(
                        (x, y, self.final_x_tile_size, self.tile_size))
                elif index_y == len(y_coords) - 1:
                    # print "final y tile %d %d %d %d"
                    # %(x,y,self.tile_size,self.final_y_tile_size)
                    self.coordinates.append(
                        (x, y, self.tile_size, self.final_y_tile_size))
                else:
                    self.coordinates.append(
                        (x, y, self.tile_size, self.tile_size))
        self.coordinates.sort()

    def crop(self):
        for coordinate in self.coordinates:
            x_coord, y_coord, width, height = coordinate
            filename = str(x_coord) + "_" + str(y_coord) + "_" + \
                str(width) + "_" + str(height) + ".png"
            path = os.path.join(os.getcwd(), "tiles", filename)
            print path
            box = (x_coord, y_coord, x_coord + width, y_coord + height)
            self.img.crop(box).save(path)

if __name__ == "__main__":
    # NOTE: Change this!!
    Tiler = ImageTiler(1000, 450, "Periodic_table_large.png")
    Tiler.ComputeCoordinates()
    Tiler.crop()
