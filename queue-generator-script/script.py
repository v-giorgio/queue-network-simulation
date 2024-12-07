# pip install graphviz (mas tem que instalar manualmente e adicionar no PATH - precisa do executavel pra funcionar)
from graphviz import Digraph

def generate_open_queue_network():
    dot = Digraph(comment="Rede aberta de filas", format="png")

    dot.attr(rankdir='LR', splines='true', nodesep='1', ranksep='1.5')

    dot.node("Chegada", shape="ellipse", label="λ = 2 jobs/s", color="blue")
    dot.node("Saída", shape="doublecircle", color="red")

    with dot.subgraph(name='cluster_S1') as s1:
        s1.attr(style='filled', color='lightgrey')
        s1.node("S1", shape="box", label="S1")
        s1.node("F1_1", shape="circle", label="Fila 1")
        s1.edges([("F1_1", "S1")])

    with dot.subgraph(name='cluster_S2') as s2:
        s2.attr(style='filled', color='lightblue')
        s2.node("S2", shape="box", label="S2")
        s2.node("F2_1", shape="circle", label="Fila 2")
        s2.edges([("F2_1", "S2")])

    with dot.subgraph(name='cluster_S3') as s3:
        s3.attr(style='filled', color='lightgreen')
        s3.node("S3", shape="box", label="S3")
        s3.node("F3_1", shape="circle", label="Fila 3")
        s3.edges([("F3_1", "S3")])

    dot.edge("Chegada", "F1_1")
    dot.edge("S1", "F2_1", label="p=0.5")
    dot.edge("S1", "F3_1", label="p=0.5")
    dot.edge("S2", "Saída", label="p=0.8")
    dot.edge("S3", "Saída")

    dot.edge("S2", "F2_1", label="p=0.2")

    return dot

graph = generate_open_queue_network()
graph.render("open_queue_network", view=True)
