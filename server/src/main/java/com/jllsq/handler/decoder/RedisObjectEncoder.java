package com.jllsq.handler.decoder;

import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RedisObjectEncoder extends MessageToByteEncoder<RedisObject> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RedisObject msg, ByteBuf out) throws Exception {
        Object ptr = msg.getPtr();
        if (msg.getEncoding() == RedisObject.REDIS_ENCODING_RAW) {
            if (msg.isShared()){
                out.writeBytes(((SDS)ptr).getBytes());
            }
            else {
                if (msg.getType() == RedisObject.REDIS_STRING) {
                    byte[] temp = ((SDS)ptr).getBytes();
                    byte[] content = new byte[((SDS)ptr).getUsed()+5];
                    content[0]='+';
                    content[1]='"';
                    content[content.length-3] = '"';
                    content[content.length-1] = '\n';
                    content[content.length-2] = '\r';
                    System.arraycopy(temp,0,content,2,((SDS)ptr).getUsed());
                    out.writeBytes(Unpooled.copiedBuffer(content));
                }
            }
        } else if (msg.getEncoding() == RedisObject.REDIS_ENCODING_INT) {
            byte[] bytes = ptr.toString().getBytes();
            byte[] content = new byte[bytes.length+3];
            System.arraycopy(bytes,0,content,1,bytes.length);
            content[0]=':';
            content[content.length-1] = '\n';
            content[content.length-2] = '\r';
            out.writeBytes(Unpooled.copiedBuffer(content));
        } else if (msg.getEncoding() == RedisObject.REDIS_ENCODING_ZIPMAP) {

        } else if (msg.getEncoding() == RedisObject.REDIS_ENCODING_HT) {

        }
        return;
    }


}
