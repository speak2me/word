/**
 * 
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.apdplat.word;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import org.apdplat.word.segmentation.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将一个文件分词后保存到另一个文件
 * @author 杨尚川
 */
public class SegFile {    
    private static final Logger LOGGER = LoggerFactory.getLogger(SegFile.class);
    public static void main(String[] args) throws Exception{
        String input = "target/text.txt";
        String output = "target/word.txt";
        if(args.length == 2){
            input = args[0];
            output = args[1];
        }
        long start = System.currentTimeMillis();
        segFile(input, output);
        long cost = System.currentTimeMillis()-start;
        LOGGER.info("cost time:"+cost+" ms");
    }
    public static void segFile(String input, String output) throws Exception{
        float max=(float)Runtime.getRuntime().maxMemory()/1000000;
        float total=(float)Runtime.getRuntime().totalMemory()/1000000;
        float free=(float)Runtime.getRuntime().freeMemory()/1000000;
        String pre="执行之前剩余内存:"+max+"-"+total+"+"+free+"="+(max-total+free);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input),"utf-8"));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output),"utf-8"))){
            int textLength=0;
            long start = System.currentTimeMillis();
            String line = null;
            while((line = reader.readLine()) != null){
                textLength += line.length();
                List<Word> words = WordSeg.seg(line);
                for(Word word : words){
                    writer.write(word.getText()+" ");
                }
                writer.write("\n");
            }
            long cost = System.currentTimeMillis() - start;
            float rate = textLength/cost;
            LOGGER.info("字符数目："+textLength);
            LOGGER.info("分词耗时："+cost+" 毫秒");
            LOGGER.info("分词速度："+rate+" 字符/毫秒");
        }
        max=(float)Runtime.getRuntime().maxMemory()/1000000;
        total=(float)Runtime.getRuntime().totalMemory()/1000000;
        free=(float)Runtime.getRuntime().freeMemory()/1000000;
        String post="执行之后剩余内存:"+max+"-"+total+"+"+free+"="+(max-total+free);
        LOGGER.info(pre);
        LOGGER.info(post);
    }
}
