package ru.yandex.practicum.filmorate.dao.mpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MpaDaoImplTest {

    @Autowired
    private MpaDaoImpl mpaDao;

    @Test
    @DisplayName("Получение всех MPA рейтингов из БД")
    void getAllMpa() {
        List<Mpa> mpas = mpaDao.getAllMpa();

        assertNotNull(mpas);
        assertEquals(5, mpas.size(), "В таблице должно быть 5 записей");

        List<String> names = mpas.stream()
                .map(Mpa::getName)
                .toList();

        assertTrue(names.containsAll(List.of("G", "PG", "PG-13", "R", "NC-17")));
    }

    @Test
    @DisplayName("Получение MPA по существующему ID")
    void getMpaById() {
        Optional<Mpa> mpaOpt = mpaDao.getMpaById(3L);

        assertTrue(mpaOpt.isPresent(), "MPA с id=3 должен существовать");
        assertEquals("PG-13", mpaOpt.get().getName());
    }

    @Test
    @DisplayName("Получение MPA по несуществующему ID возвращает пустой Optional")
    void getMpaByIdNotFound() {
        Optional<Mpa> mpaOpt = mpaDao.getMpaById(999L);

        assertTrue(mpaOpt.isEmpty(), "Ожидался пустой Optional для несуществующего ID");
    }
}