package ru.stqa.pft.sandbox;

class Point {
    final private double x;
    final private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //Для вычисления в методе
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    //Для вычисления в классе
    public double distanceClass(Point p) {
        double dX = this.x - p.x;
        double dY = this.y - p.y;
        return Math.sqrt(dX * dX + dY * dY);
    }
}

class Run {
    public static void main(String[] args) {
//        logger.info("Пример логирования с использованием SLF4J и Logback");

        Point point1 = new Point(1, 6);
        Point point2 = new Point(2, 3);

        double d1 = distance(point1, point2);
        System.out.println("Расстояние между точками в методе = " + d1);

        double d2 = point1.distanceClass(point2);
        System.out.println("Расстояние между точками в методе класса = " + d2);

    }

    public static double distance(Point p1, Point p2) {
        double dX = p1.getX() - p2.getX();
        double dY = p1.getY() - p2.getY();
        return Math.sqrt(dX * dX + dY * dY);
    }
}


