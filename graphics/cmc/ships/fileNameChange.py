import os

dirPath = os.path.dirname(os.path.realpath(__file__))


def main():
    print("Root:" + dirPath)
    for root, subfolders, filenames in os.walk(dirPath):
        changed = []
        for filename in filenames:
            filePath = os.path.join(root, filename)
            if filePath in changed:
                continue
            (shortname, extension) = os.path.splitext(filename)
            if(shortname.endswith("_m")):
                tempPath = os.path.join(root, shortname[0:-2] + "_g" + extension)
                try:
                    os.rename(filePath, tempPath)
                except Exception as e:
                    print(e)
                    print('rename file fail\r\n')
    input("Press Any key To Exit.")


if __name__ == '__main__':
    main()
