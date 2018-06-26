package com.ex.myapplication;

import com.ex.hiworld.server.tools.LogsUtils;

import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleProtocol {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

//        tString2Byts();

        tByteBreakCombine();

    }

    private void tByteBreakCombine() {
        int[][] Arr = new int[10][];
        for (int i = 0; i < Arr.length; i++) {
            if (i == 1)
                Arr[i] = new int[]{0x5A, 0xA5, 0x09, 0x67, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x6F};
            else if (i == 2)
                Arr[i] = new int[]{0x5A, 0xA5, 0x0E, 0x47, 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            else if (i == 3)
                Arr[i] = new int[]{0xD4};
            else if (i == 4)
                Arr[i] = new int[]{0x5A, 0xA5, 0x04, 0x76, 0x08, 0x00, 0x00, 0x00, 0x81};
            else if (i == 5)
                Arr[i] = new int[]{0x5A, 0xA5, 0x04, 0x76, 0x08, 0x00, 0x00};
            else if (i == 6)
                Arr[i] = new int[]{0x00};
            else if (i == 7)
                Arr[i] = new int[]{0x81};
            else if (i == 8)
                Arr[i] = new int[]{0x5A, 0xA5, 0x04, 0x76, 0x08, 0x00, 0x00, 0x00, 0x81};
            else
                Arr[i] = new int[]{0x5A, 0xA5, 0x0C, 0x31, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, 0x25, 0x25, 0x00, 0x00, 0x25, 0x83, 0x3E};
        }

        for (int[] aa : Arr) {
            checkAndGetFullData(aa);
        }
    }

    boolean isFullData = false;
    int[] tempData = new int[1024];
    int offset = 0;

    private void checkAndGetFullData(int[] ints) {
        if (ints.length > 4 && ints[0] == 0x5A && ints[1] == 0xA5) {
            if (ints[2] != ints.length - 5) {  // tou 2, len 1, cmd 1, checksum 1;
                isFullData = false;
                System.arraycopy(ints, 0, tempData, offset, ints.length);
                offset = ints.length;
                return;
            }
            System.arraycopy(ints, 0, tempData, offset, ints.length);
        } else {
            // not standard data , may be a remaining data of last data.
            if (!isFullData) {
                System.arraycopy(ints, 0, tempData, offset, ints.length);
            }
            boolean fullData = isFullData(tempData);
            isFullData = fullData;
            if (!isFullData) {
                offset += ints.length;
                return;
            }
        }

        offset = 0;
        isFullData = true;
        int reallen = tempData[2] + 5;
        int data[] = new int[reallen];
        System.arraycopy(tempData, 0, data, 0, reallen);
        Arrays.fill(tempData, 0);
        System.out.println("complete data: " + LogsUtils.toHexString(data));
    }

    private boolean isFullData(int[] dd) {
        if (dd.length > 4 && dd[0] == 0x5A && dd[1] == 0xA5) {
            return checkOk(dd, dd[2]);
        }
        return false;
    }

    private boolean checkOk(int[] dd, int len) {
        int chck = 0;
        for (int i = 0; i < len; i++) {
            chck += dd[2 + i];
        }
        chck = (chck - 1) & 0xFF;
        return chck == dd[len + 4];
    }

    private void tString2Byts() {
        String ss = "87.5 MHz";
        char[] chars = ss.toCharArray();
        StringBuffer sou = new StringBuffer();
        sou.append("{");
        for (char c : chars) {
            byte b = (byte) c;
            sou.append(" " + Integer.toHexString(b));
        }
        System.out.println(sou.toString());
        byte[] bytes = ss.getBytes();
        sou = new StringBuffer();
        sou.append("{");
        for (byte b : bytes) {
            sou.append(" " + Integer.toHexString(b));
        }
        System.out.println(sou.toString());


        for (byte b : bytes) {
            sou.append(b);
        }
        System.out.println("... " + sou.length());
        for (int i = 0; i < 5; i++)
            sou.append(0);

        byte[] newB = sou.toString().getBytes();
        System.out.println(".... new : " + newB.length + ", " + sou.toString());
    }
}