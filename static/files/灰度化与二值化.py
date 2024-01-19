from tkinter import filedialog
import cv2
import tkinter as tk
from PIL import Image, ImageTk

# 定义全局变量
img = None

class ImageLabel(tk.Label):
    def __init__(self, master=None, **kwargs):
        tk.Label.__init__(self, master, **kwargs)
        self.image = None

    def update_image(self, image):
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image = Image.fromarray(image)
        image = ImageTk.PhotoImage(image)

        self.configure(image=image)
        self.image = image
        self.update_idletasks()

def upload_image():
    global img
    file_path = filedialog.askopenfilename()
    if file_path:
        img = cv2.imread(file_path)
        original_label.update_image(img)

def show_orginal_image():
    if img is not None:
        original_label.update_image(img)

def show_gray_image():
    if img is not None:
        gray_image = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        gray_label.update_image(gray_image)

def show_binary_image():
    if img is not None:
        gray_image = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        _, binary_image = cv2.threshold(gray_image, 128, 255, cv2.THRESH_BINARY)
        binary_label.update_image(binary_image)

def show_both_images():
    show_gray_image()
    show_binary_image()

# 创建主窗口
root = tk.Tk()
root.title("Image Viewer")

# 添加按钮
upload_button = tk.Button(root, text="Upload Image", command=upload_image)
upload_button.pack(pady=10)

original_label = ImageLabel(root)
original_label.pack(pady=10)

original_button = tk.Button(root, text="Show Image", command=show_orginal_image)
original_button.pack(pady=10)

gray_label = ImageLabel(root)
gray_label.pack(pady=10,padx=100)

gray_button = tk.Button(root, text="Show Gray Image", command=show_gray_image)
gray_button.pack(pady=10,padx=100)

binary_label = ImageLabel(root)
binary_label.pack(pady=10,padx=100)

binary_button = tk.Button(root, text="Show Binary Image", command=show_binary_image)
binary_button.pack(pady=10,padx=100)

both_button = tk.Button(root, text="Show Both Images", command=show_both_images)
both_button.pack(pady=10,padx=100)

# 运行主循环
root.mainloop()
