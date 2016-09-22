from PIL import Image

class ImageTiler(object):
    def __init__(self, tile_size, overlap, filename):
        self.tile_size = tile_size
        self.overlap = overlap
        self.img = Image.open(filename)
        self.img_width, self.img_height = self.img.size
        self.final_x_tile_size = self.img_width % self.tile_size
        self.final_y_tile_size = self.img_height % self.tile_size
        self.coordinates = [(0, 0, tile_size, tile_size)]
        self.ComputeCoordinates()

    def ComputeCoordinates(self):
        x_coords, y_coords = [], []
        curr_y, curr_x = self.tile_size, self.tile_size
        while curr_x < self.img_width:
            next_x = curr_x - self.overlap
            x_coords.append(next_x)
            curr_x = next_x + self.tile_size
        if curr_x > self.img_width:
            x_coords.append(self.final_x_tile_size)
        while curr_y < self.img_height:
            next_y = curr_y - self.overlap
            y_coords.append(next_y)
            curr_y = next_y + self.tile_size
        if curr_y > self.img_height:
            y_coords.append(self.final_y_tile_size)
        print x_coords, y_coords
        for index_x, x in enumerate(x_coords):
            for index_y, y in enumerate(y_coords):
                if index_x == len(x_coords) - 1 and index_y == len(y_coords) - 1 and self.final_x_tile_size and self.final_y_tile_size:
                        self.coordinates.append((x, y, self.final_x_tile_size, self.final_y_tile_size))
                elif index_x == len(x_coords) - 1 and self.final_x_tile_size:
                    self.coordinates.append((x, y, self.final_x_tile_size, self.tile_size))
                elif index_y == len(y_coords) - 1 and self.final_y_tile_size:
                    self.coordinates.append((x, y, self.tile_size, self.final_y_tile_size))
                else:
                    self.coordinates.append((x, y, self.tile_size, self.tile_size))


    def crop(self, path, input):
        pass


        # coordinates = {}
        # while curr_x > self.img_width:
        #
        # for i in range(0,imgheight,height):
        #     for j in range(0,imgwidth,width):
        #         box = (j, i, j+width, i+height)
        #         a = img.crop(box)
        #         try:
        #             o = a.crop(area)
        #             o.save(os.path.join(path,"PNG","%s" % page,"IMG-%s.png" % k))
        #         except:
        #             pass
        #         k +=1

if __name__ == "__main__":
    Tiler = ImageTiler(1000, 450, "Periodic_table_large.png")
    print Tiler.img_width, Tiler.img_height, Tiler.tile_size, Tiler.final_x_tile_size, Tiler.final_y_tile_size
    print Tiler.coordinates
