import tkinter as tk

from data_types import Point, Segment
from bentley_ottmann import find_intersections


class MainWindow:
    RADIUS = 5

    def __init__(self, master, segments=None, box=None):
        if segments is None:
            segments = []
        self.segments = segments
        self.points = []
        self.box = box
        self.master = master
        self.master.title("LAB2")

        self.frmMain = tk.Frame(self.master, relief=tk.RAISED, borderwidth=1)
        self.frmMain.pack(fill=tk.BOTH, expand=1)

        self.w = tk.Canvas(self.frmMain, width=900, height=900)
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
        self.draw_segments(*self.segments)
        self.calculate()

    def calculate(self):
        if not self.segments:
            return
        self.w.delete(tk.ALL)
        print(self.segments)
        self.draw_segments(*self.segments)
        points = find_intersections(*self.segments)
        print(points)
        self.draw_points(*points)

    def clear(self):
        self.w.delete(tk.ALL)
        self.segments.clear()
        self.points.clear()

    def spawn_point(self, event):
        point = Point(event.x, event.y)
        if self.points:
            ui_point = self.points.pop(0)
            segment = Segment(ui_point, point)
            self.segments.append(segment)
            self.draw_segments(segment)
            self.w.delete(ui_point.ui_id)
            return
        self.points.append(point)
        self.draw_points(point)

    def motion(self, event):
        x, y = event.x, event.y
        self.master.title('{}, {}'.format(x, y))

    def draw_segments(self, *segments: Segment):
        for s in segments:
            self.w.create_line(s.start.x, s.start.y, s.end.x, s.end.y, fill='blue', width=2)

    def draw_points(self, *points: Point):
        for point in points:
            ui_id = self.w.create_oval(point.x - self.RADIUS, point.y - self.RADIUS,
                                       point.x + self.RADIUS, point.y + self.RADIUS,
                                       fill="red")
            point.ui_id = ui_id


def main():
    root = tk.Tk()
    initial_segments = [
        Segment(Point(608, 507), Point(748, 510)),
        Segment(Point(158, 759), Point(264, 581)),
        Segment(Point(230, 167), Point(398, 709)),
        Segment(Point(333, 877), Point(883, 588)),
        Segment(Point(192, 653), Point(779, 668))
    ]
    # initial_segments = [
    #     Segment(Point(210, 702), Point(567, 388)),
    #     Segment(Point(166, 493), Point(544, 685)),
    #     Segment(Point(266, 458), Point(510, 521)),
    #     # Segment(Point(182, 621), Point(456, 420)),
    # ]
    MainWindow(root, segments=initial_segments)
    root.mainloop()


if __name__ == '__main__':
    main()
