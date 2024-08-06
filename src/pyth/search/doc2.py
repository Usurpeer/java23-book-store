from gensim.models.doc2vec import Doc2Vec, TaggedDocument
from nltk.tokenize import word_tokenize
from sklearn.metrics.pairwise import cosine_similarity
from collections import defaultdict

# Предобработка текста
def preprocess_text(text):
    tokens = word_tokenize(text.lower())
    return tokens

# Функция для выполнения интеллектуального поиска
def searching(query, books):
    relevance_map = defaultdict(float)
    # Предобработка запроса
    query_tokens = preprocess_text(query)
    # Загрузка модели Doc2Vec
    model = Doc2Vec.load("path_to_doc2vec_model")  # Замените "path_to_doc2vec_model" на путь к вашей обученной модели Doc2Vec
    # Получение векторного представления запроса
    query_vector = model.infer_vector(query_tokens)
    for book in books:
        # Составление текста книги из всех её полей
        book_text = book['title'] + ' ' + book['description'] + ' ' + ' '.join(book['authors']) + ' ' + ' '.join(book['genres']) + ' ' + book['publisher']
        # Предобработка текста книги
        book_tokens = preprocess_text(book_text)
        # Получение векторного представления книги
        book_vector = model.infer_vector(book_tokens)
        # Вычисление косинусной схожести между запросом и книгой
        relevance = cosine_similarity([query_vector], [book_vector])[0][0]
        relevance_map[book['id']] = relevance
    return relevance_map