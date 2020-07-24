import sys
import cv2
from PIL import Image
from lines.lines import find_lines
from ayat.ayat import find_ayat, process

hafs_ayat = [7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111,
             43, 52, 99, 128, 111, 110, 98, 135, 112, 78, 118, 64, 77,
             227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75,
             85, 54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55,
             78, 96, 29, 22, 24, 13, 14, 11, 11, 18, 12, 12, 30, 52, 52,
             44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25,
             22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11,
             11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6]
warsh_ayat = [7, 285, 200, 175, 122, 167, 206, 76, 130, 109, 121, 111,
              44, 54, 99, 128, 110, 105, 99, 134, 111, 76, 119, 62, 77,
              226, 95, 88, 69, 59, 33, 30, 73, 54, 46, 82, 182, 86, 72,
              84, 53, 50, 89, 56, 36, 34, 39, 29, 18, 45, 60, 47, 61, 55,
              77, 99, 28, 21, 24, 13, 14, 11, 11, 18, 12, 12, 31, 52, 52,
              44, 30, 28, 18, 55, 39, 31, 50, 40, 45, 42, 29, 19, 36, 25,
              22, 17, 19, 26, 32, 20, 15, 21, 11, 8, 8, 20, 5, 8, 9, 11,
              10, 8, 3, 9, 5, 5, 6, 3, 6, 3, 5, 4, 5, 6]

ayah_markers_per_page_naskh_13 = [
		7, 4, 8, 7, 5, 5, 6, 8, 7, 7, 3, 6, 6, 5, 6, 3, 4, 5, 6, 
		3, 6, 4, 6, 5, 5, 5, 6, 2, 6, 6, 7, 4, 5, 5, 3, 5, 2, 4, 5, 
		2, 5, 7, 3, 4, 2, 3, 6, 2, 3, 2, 2, 6, 4, 2, 4, 2, 3, 2, 4, 
		3, 4, 4, 5, 2, 2, 2, 7, 5, 4, 5, 5, 5, 6, 4, 6, 5, 6, 6, 7, 
		4, 5, 5, 7, 6, 5, 6, 4, 5, 5, 7, 6, 7, 4, 5, 2, 5, 5, 5, 6, 
		5, 4, 4, 7, 4, 4, 4, 4, 1, 5, 3, 4, 1, 6, 4, 3, 5, 4, 7, 4, 
		4, 7, 6, 2, 5, 5, 3, 2, 3, 5, 2, 5, 6, 5, 6, 4, 5, 4, 5, 6, 
		5, 4, 6, 6, 4, 3, 2, 3, 1, 5, 3, 3, 4, 6, 5, 4, 5, 2, 3, 3, 
		3, 5, 4, 4, 4, 4, 5, 5, 6, 4, 3, 5, 5, 4, 3, 4, 5, 6, 7, 6, 
		7, 6, 6, 6, 6, 5, 5, 6, 3, 6, 6, 7, 3, 5, 6, 7, 4, 6, 4, 5, 
		7, 4, 3, 4, 3, 5, 4, 6, 8, 9, 5, 5, 5, 5, 4, 4, 5, 5, 5, 6, 
		5, 3, 9, 4, 5, 7, 7, 12, 9, 5, 5, 5, 4, 4, 5, 2, 4, 4, 5, 5, 
		5, 6, 6, 7, 7, 5, 6, 5, 7, 6, 5, 6, 3, 4, 6, 6, 6, 5, 4, 4, 
		4, 5, 5, 5, 4, 4, 4, 3, 4, 5, 6, 6, 4, 5, 3, 3, 5, 4, 6, 5, 
		4, 4, 6, 3, 6, 3, 5, 6, 4, 6, 5, 5, 2, 6, 5, 6, 8, 6, 6, 6, 
		6, 6, 8, 5, 6, 6, 5, 6, 5, 5, 4, 8, 5, 7, 5, 5, 7, 5, 7, 7, 
		5, 7, 5, 8, 7, 6, 7, 7, 6, 8, 5, 7, 5, 5, 5, 7, 7, 6, 4, 7, 
		5, 7, 7, 5, 8, 4, 3, 6, 5, 2, 7, 5, 4, 5, 5, 5, 4, 6, 6, 4, 
		7, 6, 7, 8, 12, 11, 13, 15, 13, 16, 14, 8, 8, 9, 6, 5, 5, 7, 9, 7, 
		6, 4, 5, 5, 6, 5, 5, 7, 6, 6, 6, 7, 6, 6, 6, 9, 7, 9, 7, 6, 
		6, 7, 7, 9, 7, 6, 8, 6, 10, 5, 4, 5, 4, 6, 7, 6, 5, 4, 8, 9, 
		7, 6, 10, 9, 8, 11, 10, 10, 10, 9, 9, 9, 10, 15, 13, 17, 13, 11, 9, 9, 
		7, 7, 8, 9, 10, 9, 8, 6, 7, 10, 8, 7, 8, 6, 11, 11, 8, 7, 7, 9, 
		9, 6, 3, 7, 5, 7, 5, 5, 5, 5, 7, 4, 7, 7, 4, 5, 13, 9, 7, 11, 
		11, 13, 10, 12, 13, 10, 9, 6, 7, 7, 6, 4, 2, 4, 5, 5, 7, 4, 3, 2, 
		4, 5, 8, 5, 9, 9, 9, 10, 9, 9, 11, 14, 15, 12, 16, 19, 20, 17, 18, 17, 
		17, 15, 17, 20, 7, 8, 5, 9, 9, 6, 6, 10, 5, 11, 10, 7, 7, 7, 5, 6, 
		5, 5, 7, 6, 6, 6, 8, 6, 4, 5, 6, 7, 6, 7, 6, 6, 6, 6, 5, 8, 
		7, 8, 6, 10, 6, 6, 7, 6, 6, 7, 8, 7, 5, 5, 7, 4, 6, 7, 7, 7, 
		7, 4, 6, 5, 6, 6, 4, 3, 10, 3, 2, 6, 8, 7, 5, 6, 5, 6, 8, 6, 
		6, 8, 6, 6, 4, 7, 9, 6, 5, 5, 11, 11, 11, 12, 9, 12, 11, 11, 17, 17, 
		20, 18, 22, 15, 21, 21, 22, 12, 12, 6, 10, 13, 15, 15, 12, 4, 4, 8, 4, 9, 
		7, 7, 7, 8, 8, 6, 7, 6, 8, 6, 6, 6, 7, 7, 8, 6, 8, 7, 7, 7, 
		4, 7, 6, 7, 6, 6, 5, 7, 6, 3, 4, 5, 5, 9, 8, 4, 7, 10, 10, 9, 
		7, 11, 10, 11, 12, 9, 14, 13, 12, 16, 9, 6, 6, 5, 6, 7, 6, 4, 5, 4, 
		6, 5, 5, 3, 9, 5, 7, 7, 6, 5, 6, 4, 4, 6, 3, 2, 6, 4, 3, 5, 
		8, 12, 13, 11, 16, 15, 14, 14, 17, 11, 14, 11, 20, 9, 19, 16, 14, 15, 14, 18, 
		18, 14, 18, 18, 18, 24, 17, 16, 18, 7, 5, 4, 5, 3, 4, 4, 3, 4, 4, 7, 
		4, 5, 4, 4, 7, 4, 3, 5, 3, 6, 5, 5, 5, 6, 5, 4, 6, 6, 6, 3, 
		4, 5, 2, 4, 4, 4, 7, 9, 9, 10, 18, 15, 12, 11, 15, 19, 16, 21, 13, 9, 
		14, 8, 8, 10, 9, 13, 7, 12, 19, 18, 16, 24, 13, 11, 11, 16, 20, 18, 24, 18, 
		25, 21, 33, 23, 20, 20, 22, 18, 20, 17, 23, 21, 23, 19, 21, 18, 21, 19, 16, 16, 
		9, 18, 19, 13, 15, 11, 12, 9
]


sura = 2
ayah = 1
# number of lines to skip when the end of the sura is reached
# for example, one for the basmallah and one for the header.
# 1 is automatically deducted from this number for sura Tawbah.
default_lines_to_skip = 2
sura_ayat = hafs_ayat

# by default, we don't increase the ayah on the top of this loop
# to handle ayat that span multiple pages - this flag allows us to
# override this.
end_of_ayah = False

lines_to_skip = 0
# warsh: 1, 560 (last page: 559)
# shamerly: 2, 523 (last page: 522) - lines to skip: 3 (2 + 1 basmala)
# qaloon: 1, 605 (last page: 604) - lines to skip: 2 (1 + 1 basmala)
for i in range(3, 4):
    image_dir = sys.argv[1] + '/'
    filename = 'pg_' + str(i) + '.png'
    # print(filename)

    # find lines
    image = Image.open(image_dir + filename).convert('RGBA')

    # note: these values will change depending on image type and size
    # warsh: 100/35/0, shamerly: 110/87/0, 175/75/1 for qaloon
    value = 87
    lines = find_lines(image, 110, value, 0)
    # print('found: %d lines on page %d' % (len(lines), i))

    img_rgb = cv2.imread(image_dir + filename)
    img_gray = cv2.cvtColor(img_rgb, cv2.COLOR_BGR2GRAY)
    template = cv2.imread(sys.argv[2], 0)
    ayat = find_ayat(img_gray, template)
    if len(ayat) != ayah_markers_per_page_naskh_13[i - 2]:
        print("found " + str(len(ayat)) + " but expected " + str(ayah_markers_per_page_naskh_13[i - 2]) + " on page " + str(i))
        sys.exit()
    # print('found: %d ayat on page %d' % (len(ayat), i))

    tpl_width, tpl_height = template.shape[::-1]

    line = 0
    current_line = 0
    x_pos_in_line = -1
    num_lines = len(lines)

    first = True
    end_of_sura = False
    for ayah_item in ayat:
        if (end_of_ayah or not first) and sura_ayat[sura - 1] == ayah:
            sura = sura + 1
            ayah = 1
            lines_to_skip = default_lines_to_skip
            if sura == 9:
                lines_to_skip = lines_to_skip - 1
            end_of_ayah = False
        elif end_of_ayah or not first:
            ayah = ayah + 1
            end_of_ayah = False
        first = False
        y_pos = ayah_item[1]

        pos = 0
        for line in range(current_line, num_lines):
            if lines_to_skip > 0:
                lines_to_skip = lines_to_skip - 1
                current_line = current_line + 1
                continue
            pos = pos + 1
            cur_line = lines[line]
            miny = cur_line[0][1]
            maxy = cur_line[1][1]
            if y_pos <= maxy:
                # we found the line with the ayah
                maxx = cur_line[1][0]
                if x_pos_in_line > 0:
                    maxx = x_pos_in_line
                minx = ayah_item[0]
                vals = (i, line + 1, sura, ayah, pos, minx, maxx, miny, maxy)
                s = 'insert into glyphs values(NULL, '
                print(s + '%d, %d, %d, %d, %d, %d, %d, %d, %d);' % vals)

                end_of_sura = False
                if sura_ayat[sura - 1] == ayah:
                    end_of_sura = True

                if end_of_sura or abs(minx - cur_line[0][0]) < tpl_width:
                    x_pos_in_line = -1
                    current_line = current_line + 1
                    if current_line == num_lines:
                        # last line, and no more ayahs - set it to increase
                        end_of_ayah = True
                else:
                    x_pos_in_line = minx
                break
            else:
                # we add this line
                maxx = cur_line[1][0]
                if x_pos_in_line > 0:
                    maxx = x_pos_in_line
                x_pos_in_line = -1
                current_line = current_line + 1
                vals = (i, line + 1, sura, ayah, pos, cur_line[0][0], maxx,
                        cur_line[0][1], cur_line[1][1])
                s = 'insert into glyphs values(NULL, '
                print(s + '%d, %d, %d, %d, %d, %d, %d, %d, %d);' % vals)

    # handle cases when the sura ends on a page, and there are no more
    # ayat. this could mean that we need to adjust lines_to_skip (as is
    # the case when the next sura header is at the bottom) or also add
    # some ayat that aren't being displayed at the moment.
    if end_of_sura:
        # end of sura always means x_pos_in_line is -1
        sura = sura + 1
        ayah = 1
        lines_to_skip = default_lines_to_skip
        if sura == 9:
            lines_to_skip = lines_to_skip - 1
        end_of_ayah = False
        while line + 1 < num_lines and lines_to_skip > 0:
            line = line + 1
            lines_to_skip = lines_to_skip - 1
        if lines_to_skip == 0 and line + 1 != num_lines:
            ayah = 0

    # we have some lines unaccounted for or stopped mid-line
    if x_pos_in_line != -1 or line + 1 != num_lines:
        if x_pos_in_line == -1:
            line = line + 1
        pos = 0
        ayah = ayah + 1
        for l in range(line, num_lines):
            cur_line = lines[l]
            pos = pos + 1
            maxx = cur_line[1][0]
            if x_pos_in_line > 0:
                maxx = x_pos_in_line
                x_pos_in_line = -1
            vals = (i, l + 1, sura, ayah, pos, cur_line[0][0], maxx,
                    cur_line[0][1], cur_line[1][1])
            s = 'insert into glyphs values(NULL, '
            print(s + '%d, %d, %d, %d, %d, %d, %d, %d, %d);' % vals)
