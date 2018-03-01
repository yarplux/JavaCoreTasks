package Hometasks.task180126;


class Geks {
    private int x;
    private int y;
    private Geks[] links;
    private int m = 0;
    private boolean watch = true;

    Geks(int ny, int nx){
        this.x = nx;
        this.y = ny;
    }

    void setLinks(Geks[] nlinks){
        this.links = nlinks;
    }

    int getX(){
        return this.x;
    }

    int getY(){
        return this.y;
    }

    boolean getWatch() {
        return this.watch;
    }

    int getMark () {
        return this.m;
    }

    void setMark(int m) {
        this.m = m;
    }

    void setWatch(boolean watched) {
        this.watch = watched;
    }

    Geks[] getLinks(){
        return this.links;
    }


}
