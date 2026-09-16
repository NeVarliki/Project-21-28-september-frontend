# НТО 2026. II отборочный этап. Командные задания — Android Решение

## 📖 Предыстория

В компании R есть склад оборудования общего пользования: ноутбуки, проекторы, камеры, переходники. Сотрудники берут оборудование на время и возвращают обратно. Администрации компании R требуется мобильное приложение, в котором сотрудник видит, что у него на руках, и может взять свободное оборудование.

Серверная часть — отдельный репозиторий (Spring Boot, H2, Liquibase).

## 📑 Технологический стек

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- Ktor Client
- kotlinx.serialization
- Coil
- DataStore Preferences
- MVI (`State` / `Intent` / `Action` + `ViewModel`), слои `ui` → `domain` → `data`

## 📱 Экраны

| Экран | Файл | Что делает |
|---|---|---|
| **Auth** | `ui/screen/auth/AuthScreen.kt` | Ввод 4-символьного кода сотрудника. Кнопка «Войти» активна только при валидном формате. `GET /auth` → при `200` код сохраняется, переход на Main с очисткой стека. При ошибке — текст ошибки. |
| **Main** | `ui/screen/main/MainScreen.kt` | `GET /info`. Аватар, имя, отдел, кнопки «Обновить» и «Выйти». Список оборудования на руках (название, инвентарный номер, категория, дата возврата). FAB «+» → экран Take. Состояния: Loading / Error / Data (в т.ч. пустой список). |
| **Take** | `ui/screen/take/TakeScreen.kt` | `GET /equipment`. Табы с категориями, под каждой — список свободного оборудования с RadioButton, поле «Вернуть до». FAB «✓» → `POST /issue`. При успехе — назад на Main с обновлением. Состояния: Loading / Error / Empty / Data. |

## 🌐 Сеть — `data/source/NetworkDataSource.kt`

Базовый URL — `Constants.HOST` (`core/Constants.kt`), все запросы вида `{HOST}/api/{code}{path}`.

| Метод | Путь | Ответ |
|---|---|---|
| GET | `/api/{code}/auth` | `200` — код существует, `401` — нет |
| GET | `/api/{code}/info` | `UserDto` — имя, отдел, фото, `issues: List<IssueDto>` |
| GET | `/api/{code}/equipment` | `Map<category, List<EquipmentDto>>` — свободное оборудование по категориям |
| POST | `/api/{code}/issue` | тело `{equipmentId, returnDate}`; `201` — создано, `409` — уже выдано |

## 💾 Хранение

`data/repo/AuthRepository.kt` — код сотрудника в DataStore Preferences + кеш в памяти.

## 🧪 Тестовые идентификаторы

`core/TestIds.kt` — `testTag` на всех интерактивных элементах. По ним работают автотесты проверяющей системы — **не переименовывать и не удалять**.

## ▶️ Запуск

1. Запустить бэкенд (JDK 17/21, Maven):
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8090
   ```
2. Для эмулятора: `adb reverse tcp:8090 tcp:8090` либо поменять `Constants.HOST` на `http://10.0.2.2:8090`.
3. Собрать и установить:
   ```bash
   ./gradlew :app:installDebug
   ```

**Коды для входа**: `1111`, `2222`, `3333`, `4444`.
