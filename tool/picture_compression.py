import logging
import shutil
import tempfile
import tkinter as tk
from tkinter.filedialog import *

from PIL import Image

# 命令提示行的颜色代码
YELLOW = "\033[33m"
GREEN = "\033[32m"
CYAN = "\033[36m"
GRAY = "\033[37m"
RESET = "\033[0;39m"

# 临时放置处理文件的文件夹
TMP_ASSETS_DIR = tempfile.TemporaryDirectory()

# 日志初始化
logging.basicConfig(
    level=logging.INFO,
    format="{}[{}%(asctime)s{}] [{}%(threadName)s{}/{}%(levelname)s{}] [{}%(filename)s{}]: %(message)s".format(
        RESET, GRAY, RESET, YELLOW, RESET, GREEN, RESET, CYAN, RESET
    ),
    datefmt="%H:%M:%S"
)


def find_png(path="", list_in=None):
    """
    一个基本的寻找后缀为 .png 的函数
    :param path: 当前需要递推搜索的文件夹
    :param list_in: 放置文件地址列表的变量
    :return 符合条件的文件地址列表
    """
    if list_in is None:
        list_in = []
    for file in os.listdir(path):
        tmp_path = path + "/" + file
        if os.path.isdir(tmp_path):
            find_png(tmp_path, list_in)
        elif tmp_path.endswith(".png"):
            list_in.append(tmp_path)
    return list_in


if __name__ == '__main__':
    # 绘制自己的空窗口，从而避免后面对话框绘制多余的窗口
    root = tk.Tk()
    root.withdraw()
    search_dir = askdirectory(initialdir='./')

    # 如果地址不为空，开始进行遍历搜索
    if search_dir:
        # 复制到的位置，复制时该文件夹不允许存在
        # 所以直接调用临时文件夹会导致程序崩溃
        tmp_dir = TMP_ASSETS_DIR.name + "/Handle"
        # 拷贝该文件夹到临时文件夹
        shutil.copytree(search_dir, tmp_dir)
        # 获取得到符合条件的文件
        math_list = find_png(tmp_dir)
        # 两个变量，记录改变前后的文件大小
        before_total_size = 0
        after_total_size = 0

        # 遍历进行文件压缩处理
        for math in math_list:
            before_size = os.path.getsize(math)
            before_total_size += before_size
            Image.open(math).save(math)
            after_size = os.path.getsize(math)
            after_total_size += after_size
            logging.info("压缩率：%.2f%%" % ((before_size - after_size) / before_size * 100))
        logging.info("总压缩率：%.2f%%" % ((before_total_size - after_total_size) / before_total_size * 100))

        # 再度打开窗口，选择放置处理好的文件路径
        place_dir = askdirectory(initialdir=search_dir)
        if place_dir:
            shutil.copytree(tmp_dir, place_dir + "/压缩过的")
        else:
            logging.warning("未选择放置文件夹，丢弃掉该压缩结果")
    else:
        logging.warning("未选择需要处理的文件夹，退出")
