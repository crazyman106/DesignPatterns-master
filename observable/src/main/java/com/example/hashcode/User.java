package com.example.hashcode;

/**
 * author : fenzili
 * e-mail : 291924028@qq.com
 * date   : 2019/2/19 22:53
 * pkn    : com.example.hashcode
 * desc   :
 * <p>
 * 1. hashCode相等，表示两个对象在哈希结构中存储位置相等,当并不表示他们相等(equals)
 * 2. 如果equals相等，那么他们的存储位置当然相等，所以hashCode一定也是相等的
 * 所以如果重写equals()则一定要重写hashCode()
 */

public class User {

    String name;
    int age;

    /**
     * 保证equals返回为true的两个对象一定是hashCode相等的
     * hashCode 是用于散列数据的快速存取，如利用 HashSet/HashMap/Hashtable 类来存储数据时，都会根据存储对象的 hashCode 值来进行判断是否相同的
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;//为什么是31？因为这个数需要是质数 31是经验验证的一个能够很好地减小哈希碰撞的质数
        result = prime * result + age; // Integer的hashCode
        result = prime * result + ((name == null) ? 0 : name.hashCode()); // 字符串的hashCode
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof User) {
            User other = (User) obj;
            if (age != other.age)
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
