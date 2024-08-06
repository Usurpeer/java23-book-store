from transformers import BertTokenizer, BertModel
import torch

model_name = "bert-base-multilingual-cased"

# Инициализируем токенизатор и модель BERT с выбранной моделью
tokenizer = BertTokenizer.from_pretrained(model_name)
model = BertModel.from_pretrained(model_name)

def searching(search_query, books, weights=None, max_seq_length=512):
    if(weights == None):
        weights = {
            'id': 0.0,
            'title': 1.0,
            'description': 0.7,
            'authors': 1.0,
            'genres': 1.0,
            'publisher': 1.0
        }

    # словарь релевантности
    relevances = {}
    # Получаем эмбеддинг запроса
    search_tokens = tokenizer.encode(search_query, add_special_tokens=True, return_tensors='pt', max_length=max_seq_length, truncation=True)
    with torch.no_grad():
        outputs = model(search_tokens)
    query_embedding = torch.mean(outputs.last_hidden_state, dim=1).squeeze()

    for book in books:
        relevances[book['id']] = 0.0

    for book in books:
        # Вычисляем релевантность для заголовка
        title_tokens = tokenizer.encode(book['title'], add_special_tokens=True, return_tensors='pt', max_length=max_seq_length, truncation=True)
        with torch.no_grad():
            outputs = model(title_tokens)
        title_embedding = torch.mean(outputs.last_hidden_state, dim=1).squeeze()
        cosine_similarity = torch.nn.functional.cosine_similarity(query_embedding, title_embedding, dim=0)
        title_relevance = cosine_similarity.item()

        # Вычисляем релевантность для описания
        description_tokens = tokenizer.encode(book['description'], add_special_tokens=True, return_tensors='pt', max_length=max_seq_length, truncation=True)
        with torch.no_grad():
            outputs = model(description_tokens)
        description_embedding = torch.mean(outputs.last_hidden_state, dim=1).squeeze()
        cosine_similarity = torch.nn.functional.cosine_similarity(query_embedding, description_embedding, dim=0)
        description_relevance = cosine_similarity.item()

        # Вычисляем релевантность для описания
        publisher_tokens = tokenizer.encode(book['publisher'], add_special_tokens=True, return_tensors='pt', max_length=max_seq_length, truncation=True)
        with torch.no_grad():
            outputs = model(publisher_tokens)
        publisher_embedding = torch.mean(outputs.last_hidden_state, dim=1).squeeze()
        cosine_similarity = torch.nn.functional.cosine_similarity(query_embedding, publisher_embedding, dim=0)
        publisher_relevance = cosine_similarity.item()

        # Вычисляем релевантность для каждого автора
        authors_relevance = 0.0
        for author in book['authors']:
            author_tokens = tokenizer.encode(author, add_special_tokens=True, return_tensors='pt', max_length=max_seq_length, truncation=True)
            with torch.no_grad():
                outputs = model(author_tokens)
            author_embedding = torch.mean(outputs.last_hidden_state, dim=1).squeeze()
            cosine_similarity = torch.nn.functional.cosine_similarity(query_embedding, author_embedding, dim=0)
            author_relevance = cosine_similarity.item()
            authors_relevance += author_relevance

        # Вычисляем релевантность для каждого жанра
        genres_relevance = 0.0
        for genre in book['genres']:
            genre_tokens = tokenizer.encode(genre, add_special_tokens=True, return_tensors='pt', max_length=max_seq_length, truncation=True)
            with torch.no_grad():
                outputs = model(genre_tokens)
            genre_embedding = torch.mean(outputs.last_hidden_state, dim=1).squeeze()
            cosine_similarity = torch.nn.functional.cosine_similarity(query_embedding, genre_embedding, dim=0)
            genre_relevance = cosine_similarity.item()
            genres_relevance += genre_relevance

        # Учитываем релевантность для каждого поля с учетом его веса
        relevances[book['id']] += (title_relevance * weights['title'] +
                                   description_relevance * weights['description'] +
                                   authors_relevance * weights['authors'] +
                                   genres_relevance * weights['genres'] +
                                   publisher_relevance * weights['publisher'])


    # Нормализуем значения релевантности
    max_relevance = max(relevances.values())
    min_relevance = min(relevances.values())
    for book_id, relevance in relevances.items():
        relevances[book_id] = (relevance - min_relevance) / (max_relevance - min_relevance)

    filtered_relevances = {book_id: relevance for book_id, relevance in relevances.items() if relevance > 0.65}
    return filtered_relevances
