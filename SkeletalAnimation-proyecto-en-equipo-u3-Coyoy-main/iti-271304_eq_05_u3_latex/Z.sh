rm *.pdf
pdflatex -interaction nonstopmode -shell-escape -synctex=1 main.tex
bibtex main
pdflatex -interaction nonstopmode -shell-escape -synctex=1 main.tex
pdflatex -interaction nonstopmode -shell-escape -synctex=1 main.tex
rm *.aux *.bbl *.blg *.log *.out *.xlm *.nav *.toc *.vrb

echo ""
echo "Reporte compilado correctamente. El archivo se guardó como main.pdf."
