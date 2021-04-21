/**
 * Author:  Bruno
 * Created: 19/02/2018
 */
copy (
   select * from view_iqm_geral where ano = @ano and mes = @mes @filtro
) to '\\@server\tmp\iqm\@file.csv' delimiter ';' 
CSV HEADER null '' 
ENCODING 'latin1'
