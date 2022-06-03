import enum
from functools import total_ordering


class PointType(enum.Enum):
    unk = enum.auto()
    start = enum.auto()
    end = enum.auto()
    cross = enum.auto()


@total_ordering
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.segment = None
        self.cross_segment = None
        self.point_type = PointType.unk

    def set_segment(self, segment, is_start_point):
        self.segment = segment
        self.point_type = PointType.start if is_start_point else PointType.end

    def set_cross_segment(self, s1, s2):
        cross = s1.intersection(s2)
        if s2.calculate_sweep_y(cross.x - 1) < s1.calculate_sweep_y(cross.x - 1):
            s1, s2 = s2, s1
        self.segment = s1
        self.cross_segment = s2
        self.point_type = PointType.cross

    def __repr__(self):
        return f'<Point {round(self.x, 2)},{round(self.y, 2)}>'

    def __gt__(self, other):
        if self.x > other.x:
            return True
        elif self.x == other.x and self.y > other.y:
            return True
        return False

    def __eq__(self, other):
        return abs(self.x ** 2 - other.x ** 2 + self.y ** 2 - other.y ** 2) < 1e-3


class Segment:
    def __init__(self, s, e, mark_points=True):
        if e < s:
            s, e = e, s
        self.start = s
        self.end = e
        self.sx = 0

        if mark_points:
            self.start.set_segment(self, True)
            self.end.set_segment(self, False)

    def intersection(self, other):
        X1, Y1 = self.start.x, self.start.y
        X2, Y2 = self.end.x, self.end.y
        X3, Y3 = other.start.x, other.start.y
        X4, Y4 = other.end.x, other.end.y

        try:
            A1 = (Y1 - Y2) / (X1 - X2)  # Pay attention to not dividing by zero
        except ZeroDivisionError:
            Xa = X1
            Ya = (Xa - X3) / (X4 - X3) * (Y4 - Y3) + Y3
        else:
            try:
                A2 = (Y3 - Y4) / (X3 - X4)  # Pay attention to not dividing by zero
            except ZeroDivisionError:
                Xa = X3
                Ya = (Xa - X1) / (X2 - X1) * (Y2 - Y1) + Y1
            else:
                if abs(A1 - A2) < 1e-6:
                    return None  # Parallel segments

                b1 = Y2 - A1 * X2
                b2 = Y4 - A2 * X4

                Xa = (b2 - b1) / (A1 - A2)  # Once again, pay attention to not dividing by zero
                Ya = A1 * Xa + b1

        if ((Xa < max(min(X1, X2), min(X3, X4))) or
                (Xa > min(max(X1, X2), max(X3, X4)))):
            return None  # intersection is out of bound
        return Point(Xa, Ya)

    def calculate_sweep_y(self, x):
        X1, Y1 = self.start.x, self.start.y
        X2, Y2 = self.end.x, self.end.y
        try:
            y = (x - X1) / (X2 - X1) * (Y2 - Y1) + Y1
        except ZeroDivisionError:
            return float('inf')
        return y

    def __repr__(self):
        return f"<Segment {self.start}-{self.end}>"
