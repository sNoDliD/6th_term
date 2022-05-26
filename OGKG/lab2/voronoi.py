import math

from data_types import Point, Event, Arc, Segment, PriorityQueue


class Voronoi:
    def __init__(self, points, bounding_box=None):
        if bounding_box is None:
            bounding_box = (0, 0, 500, 500)
        self.output = []
        self.arc = None

        self.points = PriorityQueue()
        self.event = PriorityQueue()

        self.x0, self.y0, self.x1, self.y1 = bounding_box

        for point in points:
            if not isinstance(point, Point):
                point = Point(point[0], point[1])
            self.points.push(point)

            if point.x < self.x0:
                self.x0 = point.x
            elif point.x > self.x1:
                self.x1 = point.x
            if point.y < self.y0:
                self.y0 = point.y
            elif point.y > self.y1:
                self.y1 = point.y

    def process(self):
        while not self.points.empty():
            if not self.event.empty() and (self.event.top().x <= self.points.top().x):
                self.process_event()
            else:
                self.process_point()

        while not self.event.empty():
            self.process_event()

        self.finish_edges()

    def process_point(self):
        p = self.points.pop()
        self.arc_insert(p)

    def process_event(self):
        e = self.event.pop()
        if not e.valid:
            return
        s = Segment(e.p)
        self.output.append(s)

        a = e.a
        if a.pprev is not None:
            a.pprev.pnext = a.pnext
            a.pprev.s1 = s
        if a.pnext is not None:
            a.pnext.pprev = a.pprev
            a.pnext.s0 = s

        if a.s0 is not None:
            a.s0.finish(e.p)
        if a.s1 is not None:
            a.s1.finish(e.p)

        if a.pprev is not None:
            self.check_circle_event(a.pprev, e.x)
        if a.pnext is not None:
            self.check_circle_event(a.pnext, e.x)

    def arc_insert(self, p):
        if self.arc is None:
            self.arc = Arc(p)
            return

        i = self.arc
        while i is not None:
            flag, z = self.intersect(p, i)
            if flag:
                flag, zz = self.intersect(p, i.pnext)
                if (i.pnext is not None) and (not flag):
                    i.pnext.pprev = Arc(i.p, i, i.pnext)
                    i.pnext = i.pnext.pprev
                else:
                    i.pnext = Arc(i.p, i)
                i.pnext.s1 = i.s1

                i.pnext.pprev = Arc(p, i, i.pnext)
                i.pnext = i.pnext.pprev

                i = i.pnext

                seg = Segment(z)
                self.output.append(seg)
                i.pprev.s1 = i.s0 = seg

                seg = Segment(z)
                self.output.append(seg)
                i.pnext.s0 = i.s1 = seg

                self.check_circle_event(i, p.x)
                self.check_circle_event(i.pprev, p.x)
                self.check_circle_event(i.pnext, p.x)

                return

            i = i.pnext

        i = self.arc
        while i.pnext is not None:
            i = i.pnext
        i.pnext = Arc(p, i)

        x = self.x0
        y = (i.pnext.p.y + i.p.y) / 2.0
        start = Point(x, y)

        seg = Segment(start)
        i.s1 = i.pnext.s0 = seg
        self.output.append(seg)

    def check_circle_event(self, i, x0):
        if (i.e is not None) and (i.e.x != x0):
            i.e.valid = False
        i.e = None

        if (i.pprev is None) or (i.pnext is None):
            return

        flag, x, o = self.circle(i.pprev.p, i.p, i.pnext.p)
        if flag and (x > x0):
            i.e = Event(x, o, i)
            self.event.push(i.e)

    def circle(self, a, b, c):
        if ((b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y)) > 0:
            return False, None, None

        A = b.x - a.x
        B = b.y - a.y
        C = c.x - a.x
        D = c.y - a.y
        E = A * (a.x + b.x) + B * (a.y + b.y)
        F = C * (a.x + c.x) + D * (a.y + c.y)
        G = 2 * (A * (c.y - b.y) - B * (c.x - b.x))

        if G == 0:
            return False, None, None

        ox = (D * E - B * F) / G
        oy = (A * F - C * E) / G

        x = ox + math.sqrt((a.x - ox) ** 2 + (a.y - oy) ** 2)
        o = Point(ox, oy)

        return True, x, o

    def intersect(self, p, i):
        if i is None:
            return False, None
        if i.p.x == p.x:
            return False, None

        a = p.y
        b = p.y

        if i.pprev is not None:
            a = (self.intersection(i.pprev.p, i.p, p.x)).y
        if i.pnext is not None:
            b = (self.intersection(i.p, i.pnext.p, p.x)).y

        if ((i.pprev is None) or (a <= p.y)) and ((i.pnext is None) or (p.y <= b)):
            py = p.y
            px = (i.p.x ** 2 + (i.p.y - py) ** 2 - p.x ** 2) / (2 * i.p.x - 2 * p.x)
            res = Point(px, py)
            return True, res
        return False, None

    def intersection(self, p0, p1, l):
        p = p0
        if p0.x == p1.x:
            py = (p0.y + p1.y) / 2.0
        elif p1.x == l:
            py = p1.y
        elif p0.x == l:
            py = p0.y
            p = p1
        else:
            z0 = 2.0 * (p0.x - l)
            z1 = 2.0 * (p1.x - l)

            a = 1.0 / z0 - 1.0 / z1
            b = -2.0 * (p0.y / z0 - p1.y / z1)
            c = 1.0 * (p0.y ** 2 + p0.x ** 2 - l ** 2) / z0 - 1.0 * (
                        p1.y ** 2 + p1.x ** 2 - l ** 2) / z1

            py = 1.0 * (-b - math.sqrt(b * b - 4 * a * c)) / (2 * a)

        px = 1.0 * (p.x ** 2 + (p.y - py) ** 2 - l ** 2) / (2 * p.x - 2 * l)
        res = Point(px, py)
        return res

    def finish_edges(self):
        l = self.x1 + (self.x1 - self.x0) + (self.y1 - self.y0)
        i = self.arc
        while i.pnext is not None:
            if i.s1 is not None:
                p = self.intersection(i.p, i.pnext.p, l * 2.0)
                i.s1.finish(p)
            i = i.pnext

    def get_output(self):
        res = []
        for o in self.output:
            p0 = o.start
            p1 = o.end
            res.append((p0.x, p0.y, p1.x, p1.y))
        return res
