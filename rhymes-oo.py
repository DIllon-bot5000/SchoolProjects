"""
    File: rhymes-oo.py
    Author: Dillon Barr
    Purpose: Finds the perfect rhymes of a user input word that exist in a given text file.
    Course: CSC 120 Section: 1A Spring Semester 2019
"""
import sys

class Word:
    '''
    This class creates a word object containing a string of the word and
    a 2D list of the various pronunciations of the word.
    '''
    def __init__(self, string, list_elem):
        '''
        Initializes the object with the string representing the word and creates
        a list to hold the pronunciations of the world.
         
        Parameters: string is a string read in from the input file and
                    list_elem is a slice of a list with the pronunciation of the word.
        
        Pre-condition: Word has been called after a valid text file has been opened.
        
        Post-condition: A object representing a unique word has been created.
                    
        Returns: None
        '''
        self._string = string
        self._pronun_list = []
        self.add_to_list(list_elem)
    def get_string(self):
        return self._string
    def get_list(self):
        return self._pronun_list
    def add_to_list(self, list_elem):
        self._pronun_list.append(list_elem)
    def get_stress_and_index(self):
        """ 
        Finds the primary stress vowel in the pronunciations associated with the 
        given word. It then calls another function to find and print all the perfect rhymes.
  
        Parameters: None
        
        Pre-condition: A word object has been created and the __eq__ method has been called.
        
        Post-condition: variables representing the stress phoneme and index are returned.

        Returns: a string of the stress phoneme and the index it occurs.
        """
        for elements in self._pronun_list:
            for j in range(len(elements)):
                if '1' in elements[j]:
                    stress_vowel = elements[j]
                    stress_index = j
        return stress_vowel, stress_index
    def __eq__(self, other):
        """ 
        This function searches though the dictionary of words and pronunciations. When
        the key doesn't equal the user word it then loops through the pronunciations of the
        key and checks if the pronunciations qualify the word as a perfect rhyme and returns True or False.
  
        Parameters: None
        
        Pre-condition: a valid text file and word have been input and Word objects have been
                       created.
        
        Post-condition: Method returns True if there is a perfect match or False otherwise.
        
        Returns: True or False
        """
        stress_vowell, stress_index = self.get_stress_and_index()
        counter = 0
        element = self._pronun_list[0]
        for list in other._pronun_list:
            for syllable in range(len(list)):
                placeholder_index = 0                    
                if list[syllable] == stress_vowell:
                    placeholder_index = syllable
                    if placeholder_index and stress_index == 0:
                        break
                    if stress_index == 0 and placeholder_index !=0 and \
                    other._pronun_list[counter][placeholder_index +1] == element[stress_index +1]: \
                        return True
                    
                    elif placeholder_index == 0 and other._pronun_list[counter][placeholder_index +1:] == \
                    element[stress_index +1:]:
                        return True
                    elif other._pronun_list[counter][placeholder_index +1:] == element[stress_index +1:] and \
                    element[stress_index -1] != other._pronun_list[counter][placeholder_index - 1]:
                        return True
            counter += 1
        return False
    
class WordMap:
    '''
    This class creates an object that creates a dictionary of strings contained in the
    input file with the string as a key and a word object as the value containing the pronunciations
    of the key.
    '''
    def __init__(self):
        self._dict = {}
        self.read_pronun_file()
        self.read_and_find_rhymes()
    def read_pronun_file(self):
        """ 
        This method takes in a file from the user, checks to make sure it exists,
        and proceeds to read the file in and create Word objects using the words and 
        pronunciations in the file.
    
        Parameters: None.
        
        Pre-condition: A WordMap object has been created.
        
        Post-condition: The dictionary in WordMap has been populated with strings and Word objects.
                
        Returns: None.
        """
        try:
            file = input()
            pronun_file = open(file)
        except IOError:
            print("ERROR: Could not open file " + file)
            sys.exit(1)
        for line in pronun_file:
            line = line.strip().split()
            key = line[0]
            if key not in self._dict:
                word = Word(key, line[1:])
                self._dict[key] = word
            else:
                self._dict[key].add_to_list(line[1:])
    def read_and_find_rhymes(self):
        """ 
        This method takes in a string from the user, checks to see if it exists in the dictionary
        and then proceeds to print out all the words that are perfect rhymes by calling the __eq__ method
        in the Word class.
        
        Parameters: None.
        
        Pre-condition: A WordMap object has been created.
        
        Post-condition: All perfect rhymes with the user input word are printed out in lowercase letter.
                
        Returns: None.
        """
        try:
            user_word = input().upper()
            user_object = self._dict[user_word]
        except KeyError:
            print("ERROR: the word input by the user is not in the pronunciation dictionary " + user_word.lower())
            sys.exit(1)
        for key, value in self._dict.items():
            if key == user_word:
                continue
            if user_object == self._dict[key]:
                print(self._dict[key].get_string().lower())
        
        
    
def main():
    word_map = WordMap()

main()