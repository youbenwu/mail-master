package com.ys.mail.enums;

import com.ys.mail.util.BlankUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Desc 设置表中的类型，每新增一个类型都记录到该枚举上，方便维护
 * - 使用设置计算时，统一引用该枚举，尽量别直接使用数字
 * @Author CRH
 * @Create 2022-02-14 19:19
 */
@Getter
@AllArgsConstructor
public enum EnumSettingType implements IPairs<Integer, String, EnumSettingType> {
    zero(0),
    one(1),
    two(2),
    three(3),
    four(4),
    five(5),
    six(6),
    seven(7),
    eight(8),
    nine(9),
    ten(10),
    eleven(11),
    twelve(12),
    thirteen(13),
    fourteen(14),
    fifteen(15),
    sixteen(16),
    seventeen(17),
    eighteen(18),
    nineteen(19),
    twenty(20),
    twenty_one(21),
    twenty_two(22),
    twenty_three(23),
    twenty_four(24),
    twenty_five(25),
    twenty_six(26),
    twenty_seven(27),
    twenty_eight(28),
    twenty_nine(29),
    thirty(30),
    thirty_one(31),
    thirty_two(32),
    thirty_three(33),
    thirty_four(34),
    thirty_five(35),
    thirty_six(36),
    thirty_seven(37),
    thirty_eight(38),
    thirty_nine(39),
    forty(40),
    forty_one(41),
    forty_two(42),
    forty_three(43),
    forty_four(44),
    forty_five(45),
    forty_six(46),
    forty_seven(47),
    forty_eight(48),
    forty_nine(49),
    fifty(50),
    fifty_one(51),
    fifty_two(52),
    fifty_three(53),
    fifty_four(54),
    fifty_five(55),
    fifty_six(56),
    fifty_seven(57),
    fifty_eight(58),
    fifty_nine(59),
    sixty(60),
    sixty_one(61),
    sixty_two(62),
    sixty_three(63),
    sixty_four(64),
    sixty_five(65),
    sixty_six(66),
    sixty_seven(67),
    sixty_eight(68),
    sixty_nine(69),
    seventy(70),
    seventy_one(71),
    seventy_two(72),
    seventy_three(73),
    seventy_four(74),
    seventy_five(75),
    seventy_six(76),
    seventy_seven(77),
    seventy_eight(78),
    seventy_nine(79),
    eighty(80),
    eighty_one(81),
    eighty_two(82),
    eighty_three(83),
    eighty_four(84),
    eighty_five(85),
    eighty_six(86),
    eighty_seven(87),
    eighty_eight(88),
    eighty_nine(89),
    ninety(90),
    ninety_one(91),
    ninety_two(92),
    ninety_three(93),
    ninety_four(94),
    ninety_five(95),
    ninety_six(96),
    ninety_seven(97),
    ninety_eight(98),
    ninety_nine(99),
    one_hundred(100),
    hundred_one(101),
    ;

    private final Integer type;

    @Override
    public Integer key() {
        return this.type;
    }

    @Override
    public String value() {
        return this.name();
    }

    public static EnumSettingType getByType(String number) {
        List<EnumSettingType> collect = Arrays.stream(EnumSettingType.values())
                                              .filter(s -> s.getType().toString().equals(number))
                                              .collect(Collectors.toList());
        if (BlankUtil.isNotEmpty(collect)) return collect.get(0);
        return null;
    }
}
