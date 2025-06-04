package top.gugulh.ai.tools;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.gugulh.ai.entity.po.Course;
import top.gugulh.ai.entity.po.CourseReservation;
import top.gugulh.ai.entity.po.School;
import top.gugulh.ai.entity.query.CourseQuery;
import top.gugulh.ai.service.ICourseReservationService;
import top.gugulh.ai.service.ICourseService;
import top.gugulh.ai.service.ISchoolService;

import java.util.List;

@Slf4j
@Component
public class CourseTools {

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ISchoolService schoolService;

    @Autowired
    private ICourseReservationService courseReservationService;

    @Tool(description = "根据条件查询课程")
    public List<Course> queryCourse(@ToolParam(description = "查询的条件", required = false) CourseQuery query) {
        if (query == null) {
            return courseService.list();
        }
        QueryChainWrapper<Course> wrapper = courseService.query()
                .eq(query.getType() != null, "type", query.getType())
                .le(query.getEdu() != null, "edu", query.getEdu());

        if (CollUtil.isNotEmpty(query.getSorts())) {
            for (CourseQuery.Sort sort : query.getSorts()) {
                wrapper.orderBy(true, sort.getAsc(), sort.getField());
            }
        }
        return wrapper.list();
    }

    @Tool(description = "查询所有校区")
    public List<School> querySchool() {
        return schoolService.list();
    }

    @Tool(description = "生成预约单, 返回预约单号")
    public Integer createCourse(
            @ToolParam(description = "预约课程") String course,
            @ToolParam(description = "预约校区") String school,
            @ToolParam(description = "学生姓名") String studentName,
            @ToolParam(description = "联系电话") String contactInfo,
            @ToolParam(description = "备注", required = false) String remark
    ) {
        CourseReservation reservation = new CourseReservation();
        reservation.setCourse(course);
        reservation.setSchool(school);
        reservation.setStudentName(studentName);
        reservation.setContactInfo(contactInfo);
        reservation.setRemark(remark);
        courseReservationService.save(reservation);
        return reservation.getId();
    }
}
