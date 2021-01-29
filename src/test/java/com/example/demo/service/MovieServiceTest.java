package com.example.demo.service;

import com.example.demo.config.NaverProperties;
import com.example.demo.model.Movie;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repositoryImpl.MovieRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = MovieService.class)
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @DisplayName("평점 순으로 정렬되는지 검사")
    @Test
    void shouldSortedInOrderOfGrade(){
        //given
        String query = "테스트";
        String expectedTopRankingMovieTitle = "평점1위";
        given(movieRepository.findByQuery(anyString())).willReturn(this.getStubMovies());

        //when
        List<Movie> movies = movieService.search(query);

        //then
        Assertions.assertEquals(movies.stream().findFirst().get().getTitle(),expectedTopRankingMovieTitle);
    }

    List<Movie> getStubMovies(){
        return Arrays.asList(
                Movie.builder().title("평점0").link("http://test").userRating(0.0f).build(),
                Movie.builder().title("평점2위").link("http://test").userRating(9.3f).build(),
                Movie.builder().title("평점3위").link("http://test").userRating(8.7f).build(),
                Movie.builder().title("평점1위").link("http://test").userRating(9.7f).build()
                );
    }
}