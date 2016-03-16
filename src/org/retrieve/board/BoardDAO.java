package org.retrieve.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.print.DocFlavor.STRING;

import org.retrieve.common.SQLExecutor;
import org.retrieve.board.BoardVO;

import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener;

public class BoardDAO {
	//TF-IDF
	class tfidf{
		//문서내에서 해당 단어에 대한 빈도수를 구하기 위한 함수 
		public double tf(List<String> doc, String term) {
		    double result = 0;
		    for (String word : doc) {
		       if (term.equalsIgnoreCase(word))
		              result++;
		       }
		    return result / doc.size();
		}
		//전체 문서의 수에서 해당 단어가 포함되는 문서를 기반으로 idf를 구하는 함수 
		public double idf(List<List<String>> docs, String term) {
		    double n = 0;
		    for (List<String> doc : docs) {
		        for (String word : doc) {
		            if (term.equalsIgnoreCase(word)) {
		                n++;
		                break;
		            }
		        }
		    }
		    return Math.log(docs.size() / n);
		}
		// tf 함수와 idf를 바탕으로 tf-idf를 구하는 함수 
		public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
		    return tf(doc, term) * idf(docs, term);
		}
	}

	
	//Stemming
	static class Stemmer
	{  static private char[] b;
	   static private int i,     /* offset into b */
	               i_end, /* offset to end of stemmed word */
	               j, k;
	public static String stem;
	   private static final int INC = 50;
	                     /* unit of size whereby b is increased */
	   public Stemmer()
	   {  b = new char[INC];
	      i = 0;
	      i_end = 0;
	   }
	  
	   static public String stem(String word)
	   {
	    b = new char[INC];
	       i = 0;
	       i_end = 0;
	       j = 0;
	       k = 0;
	   
	    char[] w = word.toCharArray();
	    int len = w.length;
	   
	    add(w, len);
	   
	    stem();
	   
	    return toString2();
	   }

	   static public void add(char ch)
	   {  if (i == b.length)
	      {  char[] new_b = new char[i+INC];
	         for (int c = 0; c < i; c++) new_b[c] = b[c];
	         b = new_b;
	      }
	      b[i++] = ch;
	   }

	   static public void add(char[] w, int wLen)
	   {  if (i+wLen >= b.length)
	      {  char[] new_b = new char[i+wLen+INC];
	         for (int c = 0; c < i; c++) new_b[c] = b[c];
	         b = new_b;
	      }
	      for (int c = 0; c < wLen; c++) b[i++] = w[c];
	   }
	   
	   static public String toString2() { return new String(b,0,i_end); }

	   static public int getResultLength() { return i_end; }

	   static public char[] getResultBuffer() { return b; }

	   /* cons(i) is true <=> b[i] is a consonant. */

	   static private final boolean cons(int i)
	   {  switch (b[i])
	      {  case 'a': case 'e': case 'i': case 'o': case 'u': return false;
	         case 'y': return (i==0) ? true : !cons(i-1);
	         default: return true;
	      }
	   }

	   static private final int m()
	   {  int n = 0;
	      int i = 0;
	      while(true)
	      {  if (i > j) return n;
	         if (! cons(i)) break; i++;
	      }
	      i++;
	      while(true)
	      {  while(true)
	         {  if (i > j) return n;
	               if (cons(i)) break;
	               i++;
	         }
	         i++;
	         n++;
	         while(true)
	         {  if (i > j) return n;
	            if (! cons(i)) break;
	            i++;
	         }
	         i++;
	       }
	   }

	   /* vowelinstem() is true <=> 0,...j contains a vowel */

	   static private final boolean vowelinstem()
	   {  int i; for (i = 0; i <= j; i++) if (! cons(i)) return true;
	      return false;
	   }

	   /* doublec(j) is true <=> j,(j-1) contain a double consonant. */

	   static private final boolean doublec(int j)
	   {  if (j < 1) return false;
	      if (b[j] != b[j-1]) return false;
	      return cons(j);
	   }

	   static private final boolean cvc(int i)
	   {  if (i < 2 || !cons(i) || cons(i-1) || !cons(i-2)) return false;
	      {  int ch = b[i];
	         if (ch == 'w' || ch == 'x' || ch == 'y') return false;
	      }
	      return true;
	   }

	   static  private final boolean ends(String s)
	   {  int l = s.length();
	      int o = k-l+1;
	      if (o < 0) return false;
	      for (int i = 0; i < l; i++) if (b[o+i] != s.charAt(i)) return false;
	      j = k-l;
	      return true;
	   }

	   static private final void setto(String s)
	   {  int l = s.length();
	      int o = j+1;
	      for (int i = 0; i < l; i++) b[o+i] = s.charAt(i);
	      k = j+l;
	   }

	   /* r(s) is used further down. */

	   static private final void r(String s) { if (m() > 0) setto(s); }

	   static private final void step1()
	   {  if (b[k] == 's')
	      {  if (ends("sses")) k -= 2; else
	         if (ends("ies")) setto("i"); else
	         if (b[k-1] != 's') k--;
	      }
	      if (ends("eed")) { if (m() > 0) k--; } else
	      if ((ends("ed") || ends("ing")) && vowelinstem())
	      {  k = j;
	         if (ends("at")) setto("ate"); else
	         if (ends("bl")) setto("ble"); else
	         if (ends("iz")) setto("ize"); else
	         if (doublec(k))
	         {  k--;
	            {  int ch = b[k];
	               if (ch == 'l' || ch == 's' || ch == 'z') k++;
	            }
	         }
	         else if (m() == 1 && cvc(k)) setto("e");
	     }
	   }

	   static private final void step2() { if (ends("y") && vowelinstem()) b[k] = 'i'; }

	   static private final void step3() { if (k == 0) return; /* For Bug 1 */ switch (b[k-1])
	   {
	       case 'a': if (ends("ational")) { r("ate"); break; }
	                 if (ends("tional")) { r("tion"); break; }
	                 break;
	       case 'c': if (ends("enci")) { r("ence"); break; }
	                 if (ends("anci")) { r("ance"); break; }
	                 break;
	       case 'e': if (ends("izer")) { r("ize"); break; }
	                 break;
	       case 'l': if (ends("bli")) { r("ble"); break; }
	                 if (ends("alli")) { r("al"); break; }
	                 if (ends("entli")) { r("ent"); break; }
	                 if (ends("eli")) { r("e"); break; }
	                 if (ends("ousli")) { r("ous"); break; }
	                 break;
	       case 'o': if (ends("ization")) { r("ize"); break; }
	                 if (ends("ation")) { r("ate"); break; }
	                 if (ends("ator")) { r("ate"); break; }
	                 break;
	       case 's': if (ends("alism")) { r("al"); break; }
	                 if (ends("iveness")) { r("ive"); break; }
	                 if (ends("fulness")) { r("ful"); break; }
	                 if (ends("ousness")) { r("ous"); break; }
	                 break;
	       case 't': if (ends("aliti")) { r("al"); break; }
	                 if (ends("iviti")) { r("ive"); break; }
	                 if (ends("biliti")) { r("ble"); break; }
	                 break;
	       case 'g': if (ends("logi")) { r("log"); break; }
	   } }
	   
	   static private final void step4() { switch (b[k])
	   {
	       case 'e': if (ends("icate")) { r("ic"); break; }
	                 if (ends("ative")) { r(""); break; }
	                 if (ends("alize")) { r("al"); break; }
	                 break;
	       case 'i': if (ends("iciti")) { r("ic"); break; }
	                 break;
	       case 'l': if (ends("ical")) { r("ic"); break; }
	                 if (ends("ful")) { r(""); break; }
	                 break;
	       case 's': if (ends("ness")) { r(""); break; }
	                 break;
	   } }

	   static private final void step5()
	   {   if (k == 0) return; /* for Bug 1 */ switch (b[k-1])
	       {  case 'a': if (ends("al")) break; return;
	          case 'c': if (ends("ance")) break;
	                    if (ends("ence")) break; return;
	          case 'e': if (ends("er")) break; return;
	          case 'i': if (ends("ic")) break; return;
	          case 'l': if (ends("able")) break;
	                    if (ends("ible")) break; return;
	          case 'n': if (ends("ant")) break;
	                    if (ends("ement")) break;
	                    if (ends("ment")) break;
	                    /* element etc. not stripped before the m */
	                    if (ends("ent")) break; return;
	          case 'o': if (ends("ion") && j >= 0 && (b[j] == 's' || b[j] == 't')) break;
	                                    /* j >= 0 fixes Bug 2 */
	                    if (ends("ou")) break; return;
	                    /* takes care of -ous */
	          case 's': if (ends("ism")) break; return;
	          case 't': if (ends("ate")) break;
	                    if (ends("iti")) break; return;
	          case 'u': if (ends("ous")) break; return;
	          case 'v': if (ends("ive")) break; return;
	          case 'z': if (ends("ize")) break; return;
	          default: return;
	       }
	       if (m() > 1) k = j;
	   }
	   
	   static private final void step6()
	   {  j = k;
	      if (b[k] == 'e')
	      {  int a = m();
	         if (a > 1 || a == 1 && !cvc(k-1)) k--;
	      }
	      if (b[k] == 'l' && doublec(k) && m() > 1) k--;
	   }

	   static public void stem()
	   {  k = i - 1;
	      if (k > 1) { step1(); step2(); step3(); step4(); step5(); step6(); }
	      i_end = k+1; i = 0;
	   }

	}
	//stopword배열 
	public static String[] stopWordsofwordnet = {
		"without", "see", "unless", "due", "also", "must", "might", "like", "]", "[", "}", "{", "<", ">", "?", "\"", "\\", "/", ")", "(", "will", "may", "can", "much", "every", "the", "in", "other", "this", "the", "many", "any", "an", "or", "for", "in", "an", "an ", "is", "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren’t", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can’t", "cannot", "could",
		"couldn’t", "did", "didn’t", "do", "does", "doesn’t", "doing", "don’t", "down", "during", "each", "few", "for", "from", "further", "had", "hadn’t", "has", "hasn’t", "have", "haven’t", "having",
		"he", "he’d", "he’ll", "he’s", "her", "here", "here’s", "hers", "herself", "him", "himself", "his", "how", "how’s", "i ", " i", "i’d", "i’ll", "i’m", "i’ve", "if", "in", "into", "is",
		"isn’t", "it", "it’s", "its", "itself", "let’s", " me", "Me","more", "most", "mustn’t", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "ought", "our", "ours", "ourselves",
		"out", "over", "own", "same", "shan’t", "she", "she’d", "she’ll", "she’s", "should", "shouldn’t", "so", "some", "such", "than",
		"that", "that’s", "their", "theirs", "them", "themselves", "then", "there", "there’s", "these", "they", "they’d", "they’ll", "they’re", "they’ve","They","they",
		"this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn’t", "we", "we’d", "we’ll", "we’re", "we’ve","WE",
		"were", "weren’t", "what", "what’s", "when", "when’s", "where", "where’s", "which", "while", "who", "who’s", "whom",
		"why", "why’s", "with", "won’t", "would", "wouldn’t", "you", "you’d", "you’ll", "you’re", "you’ve", "your", "yours", "yourself", "yourselves",
		"Without", "See", "Unless", "Due", "Also", "Must", "Might", "Like", "Will", "May", "Can", "Much", "Every", "The", "In", "Other", "This", "The", "Many", "Any", "An", "Or", "For", "In", "An", "An ", "Is", "A", "About", "Above", "After", "Again", "Against", "All", "Am", "An", "And", "Any", "Are", "Aren’t", "As", "At", "Be", "Because", "Been", "Before", "Being", "Below", "Between", "Both", "But", "By", "Can’t", "Cannot", "Could",
		"Couldn’t", "Did", "Didn’t", "Do", "Does", "Doesn’t", "Doing", "Don’t", "Down", "During", "Each", "Few", "For", "From", "Further", "Had", "Hadn’t", "Has", "Hasn’t", "Have", "Haven’t", "Having",
		"He", "He’d", "He’ll", "He’s", "Her", "Here", "Here’s", "Hers", "Herself", "Him", "Himself", "His", "How", "How’s", "I ", " I", "I’d", "I’ll", "I’m", "I’ve", "If", "In", "Into", "Is",
		"Isn’t", "It", "It’s", "Its", "Itself", "Let’s", "Me", "More", "Most", "Mustn’t", "My", "Myself", "No", "Nor", "Not", "Of", "Off", "On", "Once", "Only", "Ought", "Our", "Ours", "Ourselves",
		"Out", "Over", "Own", "Same", "Shan’t", "She", "She’d", "She’ll", "She’s", "Should", "Shouldn’t", "So", "Some", "Such", "Than",
		"That", "That’s", "Their", "Theirs", "Them", "Themselves", "Then", "There", "There’s", "These", "They", "They’d", "They’ll", "They’re", "They’ve",
		"This", "Those", "Through", "To", "Too", "Under", "Until", "Up", "Very", "Was", "Wasn’t", "We", "We’d", "We’ll", "We’re", "We’ve",
		"Were", "Weren’t", "What", "What’s", "When", "When’s", "Where", "Where’s", "Which", "While", "Who", "Who’s", "Whom",
		"Why", "Why’s", "With", "Won’t", "Would", "Wouldn’t", "You", "You’d", "You’ll", "You’re", "You’ve", "Your", "Yours", "Yourself", "Yourselves","YOU","ain't","don't","without","have","WAS","was","I"
		};
	//stopword를 제거한 나머지 문자들이 담기는 배열 
	public static ArrayList<String> wordsList = new ArrayList<String>();
	//stemmer된 문자들이 담기는 배열 
	public static ArrayList<String> stemmerList = new ArrayList<String>();

	//댓글 입력에 대한 쿼리문
	private static final String INSERT = "insert into board(content,stopResult,stemmerResult) values(?,?,?)";
	private static final String readContent="select writer, content,hit from board where content like concat(?,'%')";
	//초기 리스트 페이지에서 댓글을 달기 위한 함수
	public void create(final BoardVO vo) throws Exception{
		
		
		new SQLExecutor(){

			@Override
			protected void doJob() throws Exception {
				// TODO Auto-generated method stub
				System.out.println(vo);
				pstmt = con.prepareStatement(INSERT);		
				pstmt.setString(1,vo.getContent());
				
				System.out.println(""+vo.getContent());
				
				vo.getContent().trim().replaceAll("\\s+", " ");
				String[] words =vo.getContent().split(" ");
				
				for (String word : words) {
					   wordsList.add(word);
				}
				
				for (int i = 0; i < wordsList.size(); i++) {
						//stopword 반복을 통해 제거한다.
					   for (int j = 0; j < stopWordsofwordnet.length; j++) {
					   if (stopWordsofwordnet[j].contains(wordsList.get(i))) {
					   wordsList.remove(i);
					         }
					      }
					   }
					   for (String str : wordsList) {
					   System.out.print(str+" ");
					   Stemmer.stem(str);
					   //문자를 stemming 하기위해stem 함수 호
					   stemmerList.add(Stemmer.stem(str));
					   }
				//Int형의 배열에서  문자열로 변환 
				String stopResult=wordsList.toString();
				String stemmerResult=stemmerList.toString();
				pstmt.setString(2, stopResult);
				pstmt.setString(3, stemmerResult);
				pstmt.executeUpdate();
				//wordlist배열을 초기화 
				for(int i=0;i<wordsList.size()+1;i++){
					wordsList.remove(i);
				
				}
				//sTemmerlist배열을 초기화 
				for(int i=0;i<stemmerList.size()+1;i++){
					stemmerList.remove(i);
				}
				
				
			}

			
		}.execute();
	}
	//tag된 이름을 검색하여 검색대상과 같은 내용의 결과를 리스트로 보여주는 함수 
	public List<BoardVO> tagList(final String content) throws Exception {
		//입력 받은 검색어를 스태밍한다.
		System.out.println(Stemmer.stem(content));
		System.out.println(content);
		
		final String tfdifQuery="select content from board ";
		
		//테이블의 stemmerResult라는 컬럼의 결과값을 가져오는 쿼리문 
		final String query = "select writer, content,hit from board where stemmerResult like concat('%',?,'%')";
		//검색에 대한 결과를 담는 배열
		final List<BoardVO> list = new ArrayList<BoardVO>();
		final List<String> totalList = new ArrayList<String>();
		final List<List<String>> document = Arrays.asList();

		new SQLExecutor() {

			@Override
			protected void doJob() throws Exception {
				
				//검색을 바탕으로 tf-idf를 구하는 과정이나 문제점을 해결하지 못했습니다.
//				tfidf calculator = new tfidf();
//				for (int i = 0; i < totalList.size(); i++) {
//					//document.add(totalList[i]);
//				
//				}

			    List<String> doc1 = Arrays.asList("siwoo", "babo");
			    List<String> doc2 = Arrays.asList("siwoo", "babo");
			    List<String> doc3 = Arrays.asList("siwoo", "this", "midea","is","very","fun");
			    List<List<String>> documents = Arrays.asList(doc1, doc2, doc3);
//			 
			    
//			    double tfidf1 = calculator.tfIdf(doc3, documents, "living");
//			    System.out.println("TF-IDF (game) = " + tfidf);
//			    System.out.println("TF-IDF (living) = " + tfidf1);
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, content);

				rs = pstmt.executeQuery();
				//쿼리문을 통해 나온 결과를 list에 담는 반복과정
				while (rs.next()) {
					BoardVO tagVo = new BoardVO();
				
					tagVo.setWriter(rs.getString(1));
					tagVo.setContent(rs.getString(2));
					tagVo.setHit(rs.getInt(3));
					System.out.println(tagVo);
				
					list.add(tagVo);
				}
				
				
//				pstmt1 = con.prepareStatement(tfdifQuery);
//				rs1 = pstmt1.executeQuery();
//				while(rs1.next()){
//					
//					String tf=new String();
//					BoardVO tfResult=new BoardVO();
//					tfResult.setContent(rs1.getString(1));
//					
//					tf=tfResult.getContent();
//					System.out.println(" "+tf);
//					
//					totalList.add(tf);
//					document.add(totalList);
//				}
//				
//				tfidf calculator = new tfidf();
//				System.out.println(content);
//				System.out.println("---------");
//				System.out.println(document);
//			    double tfidf = calculator.tfIdf(doc1, documents,content);
//			    System.out.println("Tf-Idf Result:"+tfidf);

			}

		}.execute();

		return list;
	}
	//전체 댓글의 리스트를 보여주는 함수
	public List<BoardVO> list(final int pageno) throws Exception {
		//pageno라는 변수값을 바탕으로 초기 보여줄 페이지를 나타냄  
		System.out.println(pageno);
		//전체 리스트를 불러오는 쿼리문 
		final String query = "select boardNo,writer,content,hit from board order by boardNo limit ?, 10;";
		//쿼리에 대한 결과를 담는 배열
		final List<BoardVO> list = new ArrayList<BoardVO>();

		new SQLExecutor() {

			@Override
			protected void doJob() throws Exception {

				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, (pageno - 1) * 10);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					BoardVO vo = new BoardVO();
				
					vo.setBoardNo(rs.getInt(1));
					vo.setWriter(rs.getString(2));
					vo.setContent(rs.getString(3));
					vo.setHit(rs.getInt(4));
					System.out.println(vo);
				
					list.add(vo);
				}

			}

		}.execute();

		return list;
	}

}
