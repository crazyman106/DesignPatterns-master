package com.example.testmodel;

import java.util.List;

/**
 * @author : fengzili on
 * @email : 291924028@qq.com
 * @date : 2019/2/22 0022
 * @pkn : com.example.testmodel
 * @desc :
 */

public class EMS {
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

            private String hotline;
            private String companyName;
            private String id;
            private List<InfoBeanX> info;

            public String getHotline() {
                return hotline;
            }

            public void setHotline(String hotline) {
                this.hotline = hotline;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<InfoBeanX> getInfo() {
                return info;
            }

            public void setInfo(List<InfoBeanX> info) {
                this.info = info;
            }

            public static class InfoBeanX {
                /**
                 * accept_address : 北京市
                 * remark : 北京邮政速递望京区域分公司马坡营投部已收件（揽投员姓名：黄调华,联系电话:18519362546）
                 * accept_time : 2019-02-16 17:50:00
                 */

                private String accept_address;
                private String remark;
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

                public String getAccept_time() {
                    return accept_time;
                }

                public void setAccept_time(String accept_time) {
                    this.accept_time = accept_time;
                }
            }
        }
    }
}
