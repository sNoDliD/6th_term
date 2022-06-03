class Tree:
    def __init__(self):
        self.values = []
        self.sweep_line = None

    def compare(self, f, s):
        f_y = f.calculate_sweep_y(self.sweep_line)
        s_y = s.calculate_sweep_y(self.sweep_line)
        if abs(f_y - s_y) < 1e-4:
            return 0
        return 1 if f_y > s_y else -1

    @property
    def debug(self):
        return [round(i.calculate_sweep_y(self.sweep_line), 2) for i in self.values]

    def insert(self, value):
        if not self.values:
            self.values.append(value)
            return value
        i = self._get_index(value)
        self.values.insert(i, value)
        return value

    def _get_index(self, value):
        l, r = 0, len(self.values)
        while l < r:
            i = l + (r - l)//2
            if self.compare(value, self.values[i]) == 1:
                l = i + 1
            else:
                r = i
        return r

    def _find(self, value):
        i = self._get_index(value)
        for j in range(i, 0, -1):
            if value == self.values[j]:
                return j
        for j in range(i, len(self.values)):
            if value == self.values[j]:
                return j
        return None

    def get_neighbors(self, value):
        left, right = None, None
        i = self._find(value)
        if i is not None:
            if i > 0:
                left = self.values[i - 1]
            if i < len(self.values) - 1:
                right = self.values[i + 1]
        return left, right

    def swap(self, s1, s2):
        i1 = self._find(s1)
        i2 = self._find(s2)
        if i1 is None or i2 is None:
            return
        self.values[i1], self.values[i2] = self.values[i2], self.values[i1]

    def remove(self, value):
        i = self._find(value)
        self.values.pop(i)
