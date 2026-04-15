# tinyWeb

Spring Boot + Magic-API + amis 低代码 Web 平台。

通过 Magic-API 动态接口引擎和 amis 低代码前端框架，实现"零代码/低代码"快速搭建业务页面和接口，同时通过 Sa-Token 提供细粒度权限控制。

## 技术栈

| 层面 | 技术 | 版本 |
|---|---|---|
| 后端框架 | Spring Boot | 2.6.13 |
| 接口引擎 | Magic-API | 2.2.2 |
| 权限框架 | Sa-Token | 1.44.0 |
| 数据库 | SQLite | 默认，兼容 MySQL |
| 模板引擎 | Thymeleaf | — |
| 前端渲染 | 百度 amis SDK | JSON 驱动的低代码前端框架 |
| 工具库 | Hutool | 5.8.16 |

## 环境要求

- JDK 1.8+
- Maven 3.x
- 无需额外安装数据库（SQLite 文件数据库随项目提供）

## 快速开始

```bash
# 克隆项目
git clone <repository-url>
cd tinyWeb

# 构建
mvn clean package

# 运行（开发模式）
mvn spring-boot:run

# 或运行打包后的 JAR
java -jar target/tinyWeb.jar
```

启动后访问 `http://localhost:18080/tiny/`

## 关键 URL

| 功能 | URL | 备注 |
|---|---|---|
| 应用首页 | `http://localhost:18080/tiny/index` | — |
| 登录页 | `http://localhost:18080/tiny/login` | 默认账号 admin / admin123 |
| Magic-API 编辑器 | `http://localhost:18080/tiny/magic` | 需要 admin 角色 |
| amis 页面查看 | `http://localhost:18080/tiny/amis/view/{id}` | — |
| amis 页面编辑 | `http://localhost:18080/tiny/amis/editor/{id}` | — |

## 项目结构

```
tinyWeb/
├── src/main/java/com/zh/nyh/
│   ├── config/                 # 配置类（鉴权、拦截器、异常处理、方言适配）
│   ├── controller/             # 页面路由与 API 端点
│   └── vo/                     # 值对象
├── src/main/resources/
│   ├── application.yaml        # 应用配置
│   ├── templates/amis/         # Thymeleaf 模板（amis 页面渲染与编辑器）
│   └── static/                 # 静态资源（amis SDK、Demo 页面）
├── tinyWeb.db                  # SQLite 数据库（含 amis 页面定义与 Magic-API 脚本）
├── fword/                      # amis-editor-demo 独立子项目（勿修改）
└── pom.xml
```

## 核心特性

### 动态接口 — Magic-API

接口脚本存储在数据库 `magic_api_file` 表中，通过 `/magic` 编辑器在线编写和调试，无需重启服务。

### 动态页面 — amis

页面 JSON 存储在数据库 `amis_page` 表中，通过 `page_type` 区分用途（菜单、页面、错误页等），运行时动态渲染。

### 动态鉴权 — Sa-Token

从数据库和 Magic-API 接口元数据动态构建鉴权路由，新增 `page_type=3` 的页面会自动纳入权限体系。

### Context-Path 自动适配

amis 渲染模板内置了 `fetcher`/`jumpTo`/`updateLocation` 拦截，所有 API 请求和页面跳转自动适配 `server.servlet.context-path` 配置，页面 JSON 中无需手动拼接路径前缀。

## 数据库

- 默认使用 SQLite（`tinyWeb.db`），连接池 Druid
- 可切换 MySQL，修改 `application.yaml` 数据源配置即可

核心表：

| 表名 | 说明 |
|---|---|
| `sys_user` | 用户表 |
| `sys_role` | 角色表 |
| `amis_page` | amis 页面定义 |
| `magic_api_file` | Magic-API 接口脚本 |

## 开发规范

- Java 1.8，不使用 var、lambda 等高版本特性
- 数据库操作通过 Magic-API 的 `SQLModule` + Hutool `Dict`，不引入 MyBatis/JPA
- 返回格式统一使用 `SaResult`
- 工具库优先使用 Hutool
- Magic-API 接口在 `/magic` 编辑器中编写，不在 Java 代码中写
- 不要修改 `fword/` 目录

## 许可证

请参考项目中的许可协议。
