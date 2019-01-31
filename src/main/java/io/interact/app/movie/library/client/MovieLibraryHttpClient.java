package io.interact.app.movie.library.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.squareup.okhttp.*;
import io.interact.app.movie.library.api.MovieService;
import lombok.*;
import okio.Buffer;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
public class MovieLibraryHttpClient implements MovieService {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    private final String serviceUrl;

    @Override
    @SneakyThrows
    public Long create(MovieDTO movieDTO) {
        val body = this.createRequestBody(movieDTO);
        val request = this.requestBuilder(null)
                .post(body)
                .build();
        val response = this.processing(request);
        String responseContent = response.body().string();
        return Long.valueOf(responseContent);
    }

    @Override
    @SneakyThrows
    public Long update(Long id, MovieDTO movieDTO) {
        val body = this.createRequestBody(movieDTO);
        val request = this.requestBuilder(id)
                .put(body)
                .build();
        val response = this.processing(request);
        if (response.code() == 204) {
            return id;
        }
        throw new MovieLibraryHttpClientException(response.code(), response.body().toString(), request);
    }

    @Override
    @SneakyThrows
    public Optional<MovieDTO> find(Long id) {
        val request = this.requestBuilder(id)
                .get()
                .build();
        val response = this.processing(request);
        String responseJson = response.body().string();
        val movieDto = mapper.readValue(responseJson, MovieDTO.class);
        if (movieDto != null) {
            return Optional.of(movieDto);
        }
        return Optional.empty();
    }

    @Override
    @SneakyThrows
    public Boolean delete(Long id) {
        val request = this.requestBuilder(id)
                .delete()
                .build();
        val response = this.processing(request);
        if (response.code() == 200) {
            return Boolean.TRUE;
        }
        if (response.code() == 204) {
            return Boolean.FALSE;
        }
        throw new MovieLibraryHttpClientException(response.code(), response.body().toString());
    }

    private Request.Builder requestBuilder(Long id) {
        val httpUrl = this.createHttpUrl(id);
        return new Request.Builder()
                .url(httpUrl);
    }

    private Response processing(Request request) throws MovieLibraryHttpClientException {
        val call = this.httpClient.newCall(request);
        try {
            val response = call.execute();
            if (response.isSuccessful()) {
                return response;
            }
            throw new MovieLibraryHttpClientException(response.code(), response.body().toString(), request);
        } catch (IOException e) {
            throw new MovieLibraryHttpClientException(request,e);
        }
    }

    @SneakyThrows
    private RequestBody createRequestBody(@NonNull MovieDTO movieDTO) {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        val json = mapper.writeValueAsString(movieDTO);
        return RequestBody.create(JSON, json);
    }

    private HttpUrl createHttpUrl(Long id) {
        val builder = HttpUrl.parse(serviceUrl)
                .newBuilder()
                .addPathSegment("movies");
        if(id != null) {
            builder.addPathSegment(id.toString());
        }
        return builder.build();
    }

    private class MovieLibraryHttpClientException extends RuntimeException {
        public MovieLibraryHttpClientException(int code, String content) {
            super("[ ERROR STATUS CODE: "+code+" ]\n\n"+content);
        }

        public MovieLibraryHttpClientException(int code, Request request) {
            super("[ ERROR STATUS CODE: "+code+" ]\n\n"+request.toString());
        }

        public MovieLibraryHttpClientException(Request request, Exception e) {
            super("REQUEST: "+ request.toString(), e);
        }

        public MovieLibraryHttpClientException(int code, String content, Request request) {
            super("[ ERROR STATUS CODE: "+code+" ]\n\n"+content+"\n\n" + request.toString()+"\n\n BODY: \n"+bodyToString(request));
        }
    }

    private static String bodyToString(final Request request){
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() != null) {
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            }
            return "N/A";
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
