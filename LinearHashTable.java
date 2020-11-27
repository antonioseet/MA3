/*
 *  Microassignment: Probing Hash Table addElement and removeElement
 *
 *  LinearHashTable: Yet another Hash Table Implementation
 * 
 *  Contributors:
 *    Bolong Zeng <bzeng@wsu.edu>, 2018
 *    Aaron S. Crandall <acrandal@wsu.edu>, 2019
 * 
 *  Copyright:
 *   For academic use only under the Creative Commons
 *   Attribution-NonCommercial-NoDerivatives 4.0 International License
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 */


class LinearHashTable<K, V> extends HashTableBase<K, V>
{
	// Linear and Quadratic probing should rehash at a load factor of 0.5 or higher
    private static final double REHASH_LOAD_FACTOR = 0.5;

    // Constructors
    public LinearHashTable()
    {
        super();
    }

    public LinearHashTable(HasherBase<K> hasher)
    {
        super(hasher);
    }

    public LinearHashTable(HasherBase<K> hasher, int number_of_elements)
    {
        super(hasher, number_of_elements);
    }

    // Copy constructor
    public LinearHashTable(LinearHashTable<K, V> other)
    {
        super(other);
	}
    
   
    // ***** MA Section Start ************************************************ //

    // Concrete implementation for parent's addElement method
    public void addElement(K key, V value)
    {
        // Check for size restrictions
        resizeCheck();
 
        // Calculate hash based on key
        int hash = super.getHash(key);

        //System.out.println("key:" +key +": " + hash);
        
        // MA TODO: find empty slot to insert (update HashItem as necessary)
        HashItem<K, V> element = super.getItems().elementAt(hash);
        
        while (!element.isEmpty())
        {
        	hash++; //move up the table
        	
            if (hash >= super.getItems().capacity()) // if we are at the end, go back to beginning 
               hash = 0;
            
            element = super.getItems().elementAt(hash);
            
            //System.out.println("key:" +key +": " + hash);

        }

        // probe the element
        super.getItems().elementAt(hash).setKey(key);
        super.getItems().elementAt(hash).setValue(value);  //Updating the attributes of HashItem if empty spot is found
        super.getItems().elementAt(hash).setIsEmpty(false);
        
        

        // Remember how many things we are presently storing (size N)
    	//  Hint: do we always increase the size whenever this function is called?
        _number_of_elements++;
        return;
    }

    // Removes supplied key from hash table
    public void removeElement(K key)
    {
        // Calculate hash from key
        int hash = super.getHash(key);

        // MA TODO: find slot to remove. Remember to check for infinite loop!
        //  ALSO: Use lazy deletion - see structure of HashItem

        System.out.println("________________________");
        System.out.println("REMOVE:" + key);
        System.out.println("________________________");
        boolean tryingToRemove = true;
        int originalHash = hash;
    	
        while(tryingToRemove)
        {
        	
        	System.out.println("HASH: "+ hash);
        	System.out.println("CAP: " + super.getItems().capacity());
        	
        	HashItem<K, V> element = super.getItems().elementAt(hash);
        	
        	if(!element.isEmpty() && element.getKey().equals(key))
        	{
        		System.out.println("FOUND AT: " + hash);
        		//element.setKey(null);
                //element.setValue(null);
                element.setIsEmpty(true);
        		tryingToRemove = false;
        	}
        	else
        	{
        		
        		hash++;
        		
        		if(hash >= super.getItems().capacity())
        		{
        			hash = 0;
        		}
        		
        		if(hash == originalHash) {
        			tryingToRemove = false;
        			//System.out.println("NO MATCH FOUND");
        		}
        		
        		
        	}
        }
        
        //System.out.println("________________________");
        //System.out.println("");
        
        
        _number_of_elements--;
        
    }
    
    // ***** MA Section End ************************************************ //
    

    // Public API to get current number of elements in Hash Table
	public int size() {
		return this._number_of_elements;
	}

    // Public API to test whether the Hash Table is empty (N == 0)
	public boolean isEmpty() {
		return this._number_of_elements == 0;
	}
    
    // Returns true if the key is contained in the hash table
    public boolean containsElement(K key)
    {
        int hash = super.getHash(key);
        HashItem<K, V> slot = _items.elementAt(hash);
        
        // Left incomplete to avoid hints in the MA :)
        return false;
    }
    
    // Returns the item pointed to by key
    public V getElement(K key)
    {
        int hash = super.getHash(key);
        HashItem<K, V> slot = _items.elementAt(hash);
        
        // Left incomplete to avoid hints in the MA :)
        return null;
    }

    // Determines whether or not we need to resize
    //  to turn off resize, just always return false
    protected boolean needsResize()
    {
        // Linear probing seems to get worse after a load factor of about 50%
        if (_number_of_elements > (REHASH_LOAD_FACTOR * _primes[_local_prime_index]))
        {
            return true;
        }
        return false;
    }
    
    // Called to do a resize as needed
    protected void resizeCheck()
    {
        // Right now, resize when load factor > 0.5; it might be worth it to experiment with 
        //  this value for different kinds of hashtables
        if (needsResize())
        {
            _local_prime_index++;

            HasherBase<K> hasher = _hasher;
            LinearHashTable<K, V> new_hash = new LinearHashTable<K, V>(hasher, _primes[_local_prime_index]);

            for (HashItem<K, V> item: _items)
            {
                if (item.isEmpty() == false)
                {
                    // Add element to new hash table
                    new_hash.addElement(item.getKey(), item.getValue());
                }
            }

            // Steal temp hash object's internal vector for ourselves
            _items = new_hash._items;
        }
    }

    // Debugging tool to print out the entire contents of the hash table
	public void printOut() {
		System.out.println(" Dumping hash with " + _number_of_elements + " items in " + _items.size() + " buckets");
		System.out.println("[X] Key	| Value	| Deleted");
		for( int i = 0; i < _items.size(); i++ ) {
			HashItem<K, V> curr_slot = _items.get(i);
			System.out.print("[" + i + "] ");
			System.out.println(curr_slot.getKey() + " | " + curr_slot.getValue() + " | " + curr_slot.isEmpty());
		}
	}
}