package com.hjs.weibo.api.model;

public class Geo
{
    String longitude;// 经度坐标

    String latitude;// 维度坐标

    String city;// 所在城市的城市代码

    String province;// 所在省份的省份代码

    String city_name;// 所在城市的城市名称

    String province_name;// 所在省份的省份名称

    String address;// 所在的实际地址，可以为空

    String pinyin;// 地址的汉语拼音，不是所有情况都会返回该字段

    String more;// 更多信息，不是所有情况都会返回该字段

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCity_name()
    {
        return city_name;
    }

    public void setCity_name(String city_name)
    {
        this.city_name = city_name;
    }

    public String getProvince_name()
    {
        return province_name;
    }

    public void setProvince_name(String province_name)
    {
        this.province_name = province_name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPinyin()
    {
        return pinyin;
    }

    public void setPinyin(String pinyin)
    {
        this.pinyin = pinyin;
    }

    public String getMore()
    {
        return more;
    }

    public void setMore(String more)
    {
        this.more = more;
    }

    @Override
    public String toString()
    {
        return "Geo [longitude=" + longitude + ", latitude=" + latitude
                + ", city=" + city + ", province=" + province + ", city_name="
                + city_name + ", province_name=" + province_name + ", address="
                + address + ", pinyin=" + pinyin + ", more=" + more + "]";
    }

}
