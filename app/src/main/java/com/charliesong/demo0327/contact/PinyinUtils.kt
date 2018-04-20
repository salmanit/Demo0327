package com.charliesong.demo0327.contact

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import kotlin.experimental.and

/**
 * Created by charlie.song on 2018/4/3.
 */
object PinyinUtils{
    /**
     * 得到 全拼
     */
    fun getPingYin(src: String): String {
        var t1: CharArray? = null
        t1 = src.toCharArray()
        var t2 = arrayOfNulls<String>(t1.size)
        val t3 = HanyuPinyinOutputFormat()
        t3.caseType = HanyuPinyinCaseType.LOWERCASE
        t3.toneType = HanyuPinyinToneType.WITHOUT_TONE
        t3.vCharType = HanyuPinyinVCharType.WITH_V
        var t4 = ""
        val t0 = t1.size
        try {
            for (i in 0 until t0) {
                // 判断是否为汉字字符
                if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+".toRegex())) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3)
                    t4 += t2[0]
                } else {
                    t4 += java.lang.Character.toString(t1[i])
                }
            }
            return t4
        } catch (e1: BadHanyuPinyinOutputFormatCombination) {
            e1.printStackTrace()
        }

        return t4
    }

    /**
     * 得到中文首字母
     *
     * @param str
     * @return
     */
    fun getPinYinHeadChar(str: String): String {

        var convert = ""
        for (j in 0 until str.length) {
            val word = str[j]
            val pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word)
            if (pinyinArray != null) {
                convert += pinyinArray[0][0]
            } else {
                convert += word
            }
        }
        return convert
    }

    /**
     * 将字符串转移为ASCII码
     */
    fun getCnASCII(cnStr: String): String {
        val strBuf = StringBuffer()
        val bGBK = cnStr.toByteArray()
        for (i in bGBK.indices) {
            strBuf.append(Integer.toHexString(bGBK[i].toInt() and  0xff))
        }
        return strBuf.toString()
    }

}