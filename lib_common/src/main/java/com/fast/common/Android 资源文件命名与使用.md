来自阿里安卓开发文档,参考部分网络文章和日常写法对齐进行补充和修改（2020-8-13）

版本说明
版本号   制定团队           更新日期        备注
1.0.1    淘宝技术团队等      2018.3.5       1) 修正部分示例和说明；2) 补充汇总参考文献到附录；3) 修正排版问题

1. 【推荐】资源文件需带模块前缀。


2. 【推荐】layout 文件的命名方式。
    Activity 的 layout 以 module_activity 开头
    Fragment 的 layout 以 module_fragment 开头
    Dialog 的 layout 以 module_dialog 开头
    include 的 layout 以 module_include 开头
    ListView 的行 layout 以 module_list_item 开头
    RecyclerView 的 item layout 以 module_recycle_item 开头
    GridView 的 item layout 以 module_grid_item 开头
    [修改] 
    ListView 的行 layout 以 module_item_list 开头
    RecyclerView 的 item layout 以 module_item_recycle 开头
    GridView 的 item layout 以 module_item_grid 开头
    
    
3. 【推荐】drawable 资源名称以小写单词+下划线的方式命名，根据分辨率不同存放在
不同的 drawable 目录下，如果介意包大小建议只使用一套，系统去进行缩放。采用
规则如下：
    模块名_业务功能描述_控件描述_控件状态限定词
    如：module_login_btn_pressed,module_tabs_icon_home_normal
    [修改] 模块名_控件描述_业务功能描述_【控件状态限定词|颜色】
    如：module_btn_login_normal
    
    [补充]
    控件描述-缩写：
    ic ----------------------icon
    bg---------------------background
    btn----------------------button
    sl-----------------------selector
    di----------------------divider
    
    sl主要用于某一view多种状态
    di主要用于分隔线
    
    控件状态限定词；
    normal	    普通状态（如果有多种状态，normal必写）
    pressed	    按下状态
    selected	选中状态
    checked	    勾选状态
    disabled	不可用状态
    focused	    聚焦状态
    
    
4. 【推荐】anim 资源名称以小写单词+下划线的方式命名，采用以下规则：
    模块名_逻辑名称_【方向|序号】
    
    Tween 动画（使用简单图像变换的动画，例如缩放、平移）资源：尽可能以通用的
    动画名称命名，如 module_fade_in, module_fade_out, module_push_down_in (动 画+方向)。
    
    Frame 动画（按帧顺序播放图像的动画）资源：尽可能以模块+功能命名+序号。如 module_loading_grey_001。
    
    [补充]
        fade_in                   淡入
        fade_out                  淡出         
        push_down_in              从下方推入
        push_down_out             从下方推出
        push_left                 推像左方
        slide_in_from_top         从头部滑动进入
        zoom_enter                变形进入
        slide_in                  滑动进入
        shrink_to_middle          中间缩小
    
    
5. 【推荐】color 资源使用#AARRGGBB 格式，写入 module_colors.xml 文件中，命名
格式采用以下规则：
模块名_逻辑名称_颜色
如：
<color name="module_btn_bg_color">#33b5e5e5</color>

[修改]模块名_逻辑名称_颜色
如：
<color name="module_btn_bg_gray">#33b5e5e5</color>


6. 【推荐】dimen 资源以小写单词+下划线方式命名，写入 module_dimens.xml 文件中，
采用以下规则：
模块名_描述信息
如：
<dimen name="module_horizontal_line_height">1dp</dimen>


7. 【推荐】style 资源采用“父 style 名称.当前 style 名称”方式命名，
写入module_styles.xml 文件中，首字母大写。如：
<style name="ParentTheme.ThisActivityTheme">
 …
</style>

[补充]模块命名：“模块名称.父 style 名称.当前 style 名称”。


8. 【推荐】string资源文件或者文本用到字符需要全部写入module_strings.xml文件中，
字符串以小写单词+下划线的方式命名，采用以下规则：
模块名_逻辑名称
如：module_login_tips,module_homepage_notice_desc


9. 【推荐】Id 资源原则上以驼峰法命名，View 组件的资源 id 建议以 View 的缩写作为
前缀。常用缩写表如下：

    控件                          缩写
    LinearLayout                  ll
    RelativeLayout                rl
    ConstraintLayout              cl
    ListView                      lv
    ScrollView                    sv
    TextView                      tv
    Button                        btn
    ImageView                     iv
    CheckBox                      cb
    RadioButton                   rb
    EditText                      et
    
[补充]
    ProgressBar                   pb
    SeekBar                       sb         
    Spinner                       spn
            
            
其它控件的缩写推荐使用小写字母并用下划线进行分割，例如：ProgressBar 对应
的缩写为 progress_bar；DatePicker 对应的缩写为 date_picker。
[修改]
其它控件的缩写推荐使用小写字母并用下划线进行分割DatePicker 对应的缩写为 date_picker。

[补充]模块_控件缩写_逻辑


10.【推荐】图片根据其分辨率，放在不同屏幕密度的 drawable 目录下管理，否则可能
在低密度设备上导致内存占用增加，又可能在高密度设备上导致图片显示不够清晰。
说明：
为了支持多种屏幕尺寸和密度，Android 提供了多种通用屏幕密度来适配。常用的
如下。
ldpi - 120dpi 
mdpi - 160dpi 
hdpi - 240dpi 
xhdpi - 320dpi 
xxhdpi - 480dpi 
xxxhdpi - 640dpi 

Android 的屏幕分辨率和密度并不存在严格的对应关系，应尽量避免直接基于分辨
率来开发，而是通过适配不同的屏幕密度来保证控件和图片的显示效果。不同密度
drawable 目录中的图片分辨率设置，参考不同密度的 dpi 比例关系。

正例：
为显示某个图标，将 48 x 48 的图标文件放在 drawable-mdpi 目录（160dpi）下； 将 72 x 72 的图标文件放在 drawable-hdpi 目录（240dpi）下；将 96 x 96 的图标
文件放在 drawable-xhdpi 目录（320dpi）下；将 144 x 144 的图标文件放在
drawable-xxhdpi 目录（480dpi）下。

反例：
上述图标，只有一个 144 x 144 的图标文件放在 drawable 目录下。

