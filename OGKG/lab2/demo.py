import tkinter as tk

from voronoi import Voronoi
from data_types import Point


class MainWindow:
    RADIUS = 3

    def __init__(self, master, points=None, box=None):
        if points is None:
            points = []
        self.points = points
        self.box = box
        self.master = master
        self.master.title("LAB2")

        self.frmMain = tk.Frame(self.master, relief=tk.RAISED, borderwidth=1)
        self.frmMain.pack(fill=tk.BOTH, expand=1)

        self.w = tk.Canvas(self.frmMain, width=500, height=500)
        self.w.config(background='white')
        self.w.bind('<Double-1>', self.spawn_point)
        self.w.bind('<Motion>', self.motion)
        self.w.pack()

        self.frmButton = tk.Frame(self.master)
        self.frmButton.pack()

        self.btnCalculate = tk.Button(self.frmButton, text='Calculate', width=25,
                                      command=self.calculate)
        self.btnCalculate.pack(side=tk.LEFT)

        self.btnClear = tk.Button(self.frmButton, text='Clear', width=25,
                                  command=self.clear)
        self.btnClear.pack(side=tk.LEFT)
        self.draw_points(*self.points)

    def calculate(self):
        if not self.points:
            return
        self.w.delete(tk.ALL)
        self.draw_points(*self.points)
        vp = Voronoi(self.points, self.box)
        vp.process()
        lines = vp.get_output()
        self.draw_lines(lines)

    def clear(self):
        self.w.delete(tk.ALL)
        self.points.clear()

    def spawn_point(self, event):
        point = Point(event.x, event.y)
        self.points.append(point)
        self.draw_points(point)

    def motion(self, event):
        x, y = event.x, event.y
        self.master.title('{}, {}'.format(x, y))

    def draw_lines(self, lines):
        for l in lines:
            self.w.create_line(l[0], l[1], l[2], l[3], fill='blue')

    def draw_points(self, *points: Point):
        for point in points:
            self.w.create_oval(point.x - self.RADIUS, point.y - self.RADIUS,
                               point.x + self.RADIUS, point.y + self.RADIUS,
                               fill="black")


def main():
    root = tk.Tk()
    initial_points = [
        Point(60, 200),
        Point(80, 240),
        Point(90, 153),
        Point(136, 153),
        Point(165, 177),
        Point(165, 217),
        Point(129, 246),
    ]
    MainWindow(root, points=initial_points)
    root.mainloop()


if __name__ == '__main__':
    main()
