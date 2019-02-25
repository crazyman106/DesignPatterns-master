package com.example.testmodel;

import java.util.List;

/**
 * @author : fengzili on
 * @email : 291924028@qq.com
 * @date : 2019/2/22 0022
 * @pkn : com.example.testmodel
 * @desc :
 */

public class ZhongTong {
    private String code;
    private List<MsgBean> msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {

        private InfoBean info;
        private List<ListInfoBean> listInfo;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public List<ListInfoBean> getListInfo() {
            return listInfo;
        }

        public void setListInfo(List<ListInfoBean> listInfo) {
            this.listInfo = listInfo;
        }

        public static class InfoBean {
        }

        public static class ListInfoBean {

            private String id;
            private String companyName;
            private String hotline;
            private List<InfoBeanX> info;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getHotline() {
                return hotline;
            }

            public void setHotline(String hotline) {
                this.hotline = hotline;
            }

            public List<InfoBeanX> getInfo() {
                return info;
            }

            public void setInfo(List<InfoBeanX> info) {
                this.info = info;
            }

            public static class InfoBeanX {
                /**
                 * accept_time : 2019-02-15 08:53:26
                 * accept_address : 丰巢的玉泉路16号院(丰巢智能快递柜)
                 * remark : 【北京市】  快件已送达【丰巢的玉泉路16号院(丰巢智能快递柜)】, 如有问题请电联（18911766892 / 4000633333,18911766892）, 感谢使用中通快递, 期待再次为您服务!
                 * opcode :
                 */

                private String accept_time;
                private String accept_address;
                private String remark;
                private String opcode;

                public String getAccept_time() {
                    return accept_time;
                }

                public void setAccept_time(String accept_time) {
                    this.accept_time = accept_time;
                }

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
            }
        }
    }
}
