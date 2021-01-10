package com.ethic.trainguide.domain;

public class Distance {

    private int distance = Integer.MAX_VALUE;

    public Distance() {

    }

    public Distance(int distance) {
        setDistance(distance);
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        // distance 0 is allowed, because
        // distance between the same station is 0
        if(distance < 0) {
            throw new IllegalArgumentException("distance must not be a negative number");
        }

        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Distance distance1 = (Distance) o;

        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return distance;
    }
}
