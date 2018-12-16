package Helpers;

/**
 * three way radix string sort algoritm
 * @Author Sedgewick & Wayne
 */
public class StringSorter {

    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    /**
     * Sorts an array of Strings alphabeticly
     * @param a a String[] that is to be sorted
     * @return a sorted String[]
     */
    public static String[] sort(String[] a) {
        sort(a, 0, a.length-1, 0);
        return a;
    }

    private static void sort(String[] a, int lo, int hi, int d) {
        if(hi <= lo) return;
        int lt = lo;
        int gt = hi;
        int v = charAt(a[lo], d);
        int i = lo + 1;
        while (i <= gt) {

            int t = charAt(a[i], d);
            if(t < v) exch(a, lt++, i++);
            else if(t > v) exch(a, i, gt--);
            else i++;
        }
        sort(a, lo, lt-1,d);
        if(v >= 0) sort(a, lt, gt, d+1);
        sort(a, gt+1, hi, d);
    }

    private static void exch(String[] a, int i, int j) {
        String swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
}
