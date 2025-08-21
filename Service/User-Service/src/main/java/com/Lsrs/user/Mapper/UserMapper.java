    package com.Lsrs.user.Mapper;

    import com.Lsrs.user.pojo.scdao;
    import com.baomidou.mybatisplus.core.mapper.BaseMapper;

    import com.example.pojo.User;
    import org.apache.ibatis.annotations.*;

    import java.time.LocalDateTime;
    import java.util.List;

    @Mapper
    public interface UserMapper  extends BaseMapper<User> {
       @Select("select user_name,user_img from sys_user.library_user where user_id=#{userId};")
       scdao getuser(String userId);

        Integer getusercount(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

        List<User> getUserList(@Param("lim")Integer lim,@Param("lim1") Integer lim1, @Param("userId")String userId,@Param("realName") String realName, @Param("phone")String phone);

        void updateUserByUserId(User user);
        @Delete("delete from sys_user.library_user where user_id=#{userId}")
        void delete(String userId);
@Update("update  sys_user.library_user set  violation_count=#{violation} where user_id=#{userId}")
        void violation(String userId,Integer violation);
       @Update("update sys_user.library_user set total_reservations=total_reservations+1 where user_id=#{userId}")
        void updatetotal(String userId);
        @Select("select violation_count from sys_user.library_user where user_id=#{userId}")
        Integer getviolation(String userId);
    }
