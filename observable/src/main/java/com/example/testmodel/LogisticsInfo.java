package com.example.testmodel;

import java.util.List;

/**
 * @author : fengzili on
 * @email : 291924028@qq.com
 * @date : 2019/2/19 0019
 * @pkn : com.ypw.lyyj.bean
 * @desc :
 */

public class LogisticsInfo {

    private int code;
    private List<MsgBean> msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String id;
        private List<InfoBean> info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * accept_address : 合肥市
             * remark : 在官网"运单资料&签收图",可查看签收人信息
             * opcode : 8000
             * accept_time : 2018-12-04 10:22:22
             */

            private String accept_address;
            private String remark;
            private String opcode;
            private String accept_time;

            public String getAccept_address() {
                return accept_address;
            }

            public void setAccept_address(String accept_address) {
                this.accept_address = accept_address;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getOpcode() {
                return opcode;
            }

            public void setOpcode(String opcode) {
                this.opcode = opcode;
            }

            public String getAccept_time() {
                return accept_time;
            }

            public void setAccept_time(String accept_time) {
                this.accept_time = accept_time;
            }
        }
    }
}
