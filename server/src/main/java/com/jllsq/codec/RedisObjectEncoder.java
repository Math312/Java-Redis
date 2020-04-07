package com.jllsq.codec;

import com.jllsq.common.basic.list.List;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.util.LongUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Iterator;

import static com.jllsq.holder.RedisServerObjectHolder.*;

public class RedisObjectEncoder extends MessageToByteEncoder<RedisObject> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RedisObject msg, ByteBuf out) throws Exception {
        if (msg == null) {
            return;
        }
        Object ptr = msg.getPtr();
        if (msg.getEncoding() == REDIS_ENCODING_RAW) {
            if (msg.isShared()){
                byte[] content = ((SDS)ptr).getBytes();
                out.writeBytes(content,8,content.length-8);
            }
            else {
                if (msg.getType() == REDIS_STRING) {
                    byte[] temp = ((SDS)ptr).getBytes();
                    byte[] content = new byte[((SDS)ptr).getUsed()+5];
                    content[0]='+';
                    content[1]='"';
                    content[content.length-3] = '"';
                    content[content.length-1] = '\n';
                    content[content.length-2] = '\r';
                    System.arraycopy(temp,8,content,2,((SDS)ptr).getUsed());
                    out.writeBytes(Unpooled.copiedBuffer(content));
                } else if (msg.getType() == REDIS_LIST) {
                    List<SDS> list = (List<SDS>) ptr;
                    Iterator<SDS> iterator = list.iterator();
                    int length = list.length();
                    int total = 1;
                    int len = LongUtils.longToBytesLength(length);
                    total += len;
                    total += 2;
                    while (iterator.hasNext()) {
                        SDS sds = iterator.next();
                        total += 1;
                        total += 1;
                        total += sds.getUsed();
                        total += 1;
                        total += 2;
                    }
                    byte[] result = new byte[total];
                    int index = 0;
                    result[0]='*';
                    index ++;
                    LongUtils.longToBytes(length,0,result,index,len);
                    index += len;
                    result[index] = '\r';
                    result[index+1] = '\n';
                    index += 2;
                    iterator = list.iterator();
                    while (iterator.hasNext()) {
                        SDS sds = iterator.next();
                        result[index] = '+';
                        index += 1;
                        result[index] = '\"';
                        index += 1;
                        System.arraycopy(sds.getBytes(),0,result,index,sds.getUsed());
                        index += sds.getUsed();
                        result[index] = '\"';
                        index += 1;
                        result[index] = '\r';
                        result[index+1] = '\n';
                        index += 2;
                    }
                    out.writeBytes(Unpooled.copiedBuffer(result));
                }
            }
        } else if (msg.getEncoding() == REDIS_ENCODING_INT) {
            byte[] bytes = ptr.toString().getBytes();
            byte[] content = new byte[bytes.length+3];
            System.arraycopy(bytes,0,content,1,bytes.length);
            content[0]=':';
            content[content.length-1] = '\n';
            content[content.length-2] = '\r';
            out.writeBytes(Unpooled.copiedBuffer(content));
        } else if (msg.getEncoding() == REDIS_ENCODING_ZIPMAP) {

        } else if (msg.getEncoding() == REDIS_ENCODING_HT) {

        }
        return;
    }


}
