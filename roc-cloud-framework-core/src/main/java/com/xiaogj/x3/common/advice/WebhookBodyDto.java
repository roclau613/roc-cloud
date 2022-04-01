package com.roc.cloud.common.advice;

public class WebhookBodyDto {
    /**
     * text，markdown，image，news
     **/
    private String msgtype;
    private WebhookTextDto text;
    private WebhookTextDto markdown;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public WebhookTextDto getText() {
        return text;
    }

    public void setText(WebhookTextDto text) {
        this.text = text;
    }

    public WebhookTextDto getMarkdown() {
        return markdown;
    }

    public void setMarkdown(WebhookTextDto markdown) {
        this.markdown = markdown;
    }

    public static class WebhookTextDto {
        private String content;
        private String[] mentioned_list;
        private String[] mentioned_mobile_list;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String[] getMentioned_list() {
            return mentioned_list;
        }

        public void setMentioned_list(String[] mentioned_list) {
            this.mentioned_list = mentioned_list;
        }

        public String[] getMentioned_mobile_list() {
            return mentioned_mobile_list;
        }

        public void setMentioned_mobile_list(String[] mentioned_mobile_list) {
            this.mentioned_mobile_list = mentioned_mobile_list;
        }
    }


}
