WINDOW = 5
LIMIT = 30
MIN_LEN = 60
FASTQ_FILE = "reads.fastq.txt"

"""
    1. Переводим строку с качеством в список значений phred+33
    2. Рассматриваем все окна длины window
    3. Если среднее качество окна <= lim,
       обрезаем прочтение с позиции начала этого окна
    4. Если такое окно не найдено, не трогаем прочтение
"""
def trim_by_quality(seq, qual, window, lim):
    scores = [ord(ch) - 33 for ch in qual]
    trim_pos = len(seq)

    for i in range(len(scores) - window + 1):
        avg = sum(scores[i:i + window]) / window
        if avg <= lim:  
            trim_pos = i
            break

    # обрезанная последовательность и строка качеств
    return seq[:trim_pos], qual[:trim_pos]


"""
    Один проход возвращает одну запись fastq:
    header, seq, plus, qual
"""
def read_fastq(path):
    with open(path, "r") as f:
        while True:

            # читаем по 4 fastq строки
            header = f.readline().rstrip("\n")
            if not header:
                break

            seq = f.readline().rstrip("\n")
            plus = f.readline().rstrip("\n")
            qual = f.readline().rstrip("\n")

            if not seq or not plus or not qual:
                raise ValueError("Некорректный FASTQ: неполная запись")

            # yield чтобы не вылезти за разумные рамки памяти
            yield header, seq, plus, qual


def round_half_up(x):
    return int(x + 0.5)

removed = 0 # число прочтений, полностью удалённых после quality trimming
lengths = [] # длины всех прочтений после quality trimming

for header, seq, plus, qual in read_fastq(FASTQ_FILE):
    trimmed_seq, trimmed_qual = trim_by_quality(seq, qual, WINDOW, LIMIT)
    trimmed_len = len(trimmed_seq)
    lengths.append(trimmed_len)

    # считаем только полностью удалённые после quality trimming
    if trimmed_len == 0:
        removed += 1

non_empty_lengths = [x for x in lengths if x > 0]

if non_empty_lengths:
    min_len = min(non_empty_lengths)
    avg_len = round_half_up(sum(non_empty_lengths) / len(non_empty_lengths))
    max_len = max(non_empty_lengths)
else:
    min_len = avg_len = max_len = 0

fin_ans = sum(1 for x in lengths if x >= MIN_LEN)

print(f"Сколько прочтений подверглось триммингу: {removed}")
print(f"Минимальная длина равна {min_len}.")
print(f"Средняя длина равна {avg_len}.")
print(f"Максимальная длина равна {max_len}.")
print(f"Оставшееся число прочтений после двух фильтраций: {fin_ans}")
