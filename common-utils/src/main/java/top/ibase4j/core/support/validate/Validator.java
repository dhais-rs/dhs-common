package top.ibase4j.core.support.validate;

import top.ibase4j.core.util.DataUtil;
import top.ibase4j.core.util.IDCardUtil;

import java.lang.reflect.Field;

/**
 * @author ShenHuaJie
 * @since 2018年8月26日 上午11:58:55
 */
public class Validator {

    private Validator() {
    }

    // 解析的入口
    public static void valid(Object object) throws Exception {
        // 获取object的类型
        Class<? extends Object> clazz = object.getClass();
        // 获取该类型声明的成员
        Field[] fields = clazz.getDeclaredFields();
        // 遍历属性
        for (Field field : fields) {
            // 对于private私有化的成员变量，通过setAccessible来修改器访问权限
            field.setAccessible(true);
            // 获取对象的成员的注解信息
            Validate validate = field.getAnnotation(Validate.class);
            if (validate != null) {
                validate(validate, field.get(object), field.getName());
            }
            // 重新设置会私有权限
            field.setAccessible(false);
        }
    }

    public static void validate(Validate validate, Object value, String filedName) throws RuntimeException {
        String description = DataUtil.isEmpty(validate.desc()) ? filedName : validate.desc();

        if (validate.nullable() && DataUtil.isEmpty(value)) {
            return;
        }

        if (!validate.nullable() && DataUtil.isEmpty(value)) {
            throw new RuntimeException(description + "不能为空");
        }

        if (validate.min() != 0 && new Double(value.toString()) < validate.min()) {
            throw new RuntimeException(description + "不能小于" + validate.min());
        }

        if (validate.max() != 0 && new Double(value.toString()) > validate.max()) {
            throw new RuntimeException(description + "不能大于" + validate.max());
        }

        if (validate.minLength() != 0 && value.toString().length() < validate.minLength()) {
            throw new RuntimeException(description + "长度不能小于" + validate.minLength());
        }

        if (validate.maxLength() != 0 && value.toString().length() > validate.maxLength()) {
            throw new RuntimeException(description + "长度不能超过" + validate.maxLength());
        }

        validate(validate.type(), value, description);

        if (DataUtil.isNotEmpty(validate.regex())) {
            if (!value.toString().matches(validate.regex())) {
                throw new RuntimeException(description + "格式不正确");
            }
        }
    }

    public static void validate(RegexType type, Object value, String description) throws RuntimeException {
        switch (type) {
        case NONE:
            break;
        case IDCARD:
            if (!IDCardUtil.isIdentity(value.toString())) {
                throw new RuntimeException(description + "格式不正确");
            }
            break;
        case DATE:
            if (!value.toString().matches(RegexType.DATE.value())) {
                throw new RuntimeException(description + "格式不正确");
            }
            break;
        case SPECIALCHAR:
            if (DataUtil.hasSpecialChar(value.toString())) {
                throw new RuntimeException(description + "不能含有特殊字符");
            }
            break;
        case PASSWORD:
            if (!DataUtil.isPassword(value.toString())) {
                throw new RuntimeException(description + "必须是大小写字母和数字的组合，长度在8-16之间");
            }
            break;
        case CHINESE:
            if (!DataUtil.isChinese(value.toString())) {
                throw new RuntimeException(description + "只能输入中文字符");
            }
            break;
        case NONECHINESE:
            if (DataUtil.isChinese2(value.toString())) {
                throw new RuntimeException(description + "不能含有中文字符");
            }
            break;
        case EMAIL:
            if (!DataUtil.isEmail(value.toString())) {
                throw new RuntimeException(description + "格式不正确");
            }
            break;
        case IP:
            if (!DataUtil.isIp(value.toString())) {
                throw new RuntimeException(description + "格式不正确");
            }
            break;
        case NUMBER:
            if (!DataUtil.isNumber(value.toString())) {
                throw new RuntimeException(description + "不是数字");
            }
            break;
        case PHONE:
            if (!DataUtil.isPhone(value.toString())) {
                throw new RuntimeException(description + "格式不正确");
            }
            break;
        case TELEPHONE:
            if (!DataUtil.isTelephone(value.toString())) {
                throw new RuntimeException(description + "格式不正确");
            }
            break;
        default:
            break;
        }
    }
}
