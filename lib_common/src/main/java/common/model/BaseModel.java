package common.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Id;



/**
 * desc:  BaseModel
 * author:  yangtao
 * <p>
 *     /**
 *  * 创建数据库实体类
 *  *
 *  *   @Entity 表示这个实体类一会会在数据库中生成对应的表，
 *
 *      @Id 表示该字段是id，注意该字段的数据类型为包装类型Long
 *
 *      @Property 则表示该属性将作为表的一个字段，其中nameInDb看名字就知道这个属性在数据库中对应的数据名称。
 *
 *      @Transient 该注解表示这个属性将不会作为数据表中的一个字段。
 *
 *      @NotNull 表示该字段不可以为空
 *
 *      @Unique 表示该字段唯一
 *
 * creat: 2019/9/19 16:05
 */
public class BaseModel implements Parcelable
{

    @Id
    public long Id;

    protected BaseModel(Parcel in) {
        Id = in.readLong();
    }

    public BaseModel() {
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel in) {
            return new BaseModel(in);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };
}
