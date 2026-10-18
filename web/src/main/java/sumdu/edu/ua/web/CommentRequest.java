package sumdu.edu.ua.web;

public record CommentRequest(long bookId, String author, String text) {}
