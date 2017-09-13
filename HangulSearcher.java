package com.how2marry.planery.util;

/**
 * Created by chihacker on 2016. 7. 31..
 *
 * Referenced by
 * @author roter
 * http://jhb.kr/122
 *
 *
 */
public class HangulSearcher {

    private static final char HANGUL_BEGIN_UNICODE = 44032;
    private static final char HANGUL_LAST_UNICODE = 55203;
    private static final char HANGUL_BASE_UNIT = 588;
    private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };


    private static boolean isInitialSound(char searchar){
        for(char c:INITIAL_SOUND){
            if(c == searchar){
                return true;
            }
        }
        return false;
    }

    private static char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }

    private static boolean isHangul(char c) {
        return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
    }

    public static HangulPos getMatchingPosition(String mvalue, String msearch){

        int st = 0;
        int vt = 0;

        String value = mvalue.toLowerCase().trim();
        String search = msearch.toLowerCase().trim();

        int seof = value.length() - search.length();
        int slen = search.length();

        if(seof < 0)
            return null;

        for(int i = 0;i <= seof;i++){
            st = 0;
            vt = 0;
            while(st < slen){


                if(isInitialSound(search.charAt(st)) && isHangul(value.charAt(i+vt))){
                    if(getInitialSound(value.charAt(i+vt))==search.charAt(st)){
                        st++;
                        vt++;
                    }
                    else
                        break;
                } else {
                    if(value.charAt(i+vt)==search.charAt(st)){
                        st++;
                        vt++;
                    }
                    else if(value.charAt(i+vt)==' '){
                        vt++;
                    }
                    else
                        break;
                }
            }

            if(st == slen){

                HangulPos hangulPos = new HangulPos();
                hangulPos.start = i;
                hangulPos.end = i + vt - 1;

                return hangulPos;
            }

        }
        return null;

    }

    public static class HangulPos{
        public int start;
        public int end;
    }

}
